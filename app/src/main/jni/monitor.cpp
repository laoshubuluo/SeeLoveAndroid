//
// Created by 简志展 on 16/3/8.
//

#include "monitor.h"

#include <jni.h>
#include <sys/select.h>
#include <unistd.h>
#include <sys/socket.h>
#include <pthread.h>
#include <signal.h>
#include <sys/wait.h>
#include <sys/types.h>
#include <sys/un.h>
#include <errno.h>
#include <stdlib.h>
#include <linux/prctl.h>
#include <stdio.h>
#include <sys/stat.h>
#include <sys/uio.h>
#include <asm/ioctls.h>
#include <sched.h>
#include <sys/syscall.h>
#include <dirent.h>

#define __ANDOID_PACKAGE_NAME  "com.tianyu.seelove"

// full path android service name
// #define __ANDROID_SERVICE_NAME   "com.example.hellojni/com.example.hellojni.MyService"

// full path android activity name
#define __ANDROID_ACTIVITY_NAME  "com.tianyu.seelove/com.tianyu.seelove.ui.activity.system.TranslateActivity"


#define __PARENT_NAME     "parent_process"
#define __CHILD_NAME      "child_process"
#define __GRAND_SON_NAME  "grand_son_process"

#define __MAX_FORK_CHILD_COUNT     20
#define __MAX_FORK_GRAND_SON_DEPTH 10

#define __INIT_PROCESS_PID  1

#define __POLLING_INTERVAL  (1000 * 1000) // 1 second

#define __PARENT_DEATH_SIGNAL  SIGUSR2

#define __EXIT_SIGNAL SIGKILL

#define __CMD_BUF_LEN  512
#define __NAME_LEN     100


static char _dir[256 + 1] = {0} ;

static const char* _fakeProcessName[] = {
        "com.tencent.mobileqq", "/init", "com.haimawanamen.haimanwanamen",  ""
} ;

static const char* gUserId           = NULL;   // same in all process, set by java code
static JNIEnv* gJniEnv               = NULL;   // NEVER used in in child/grand_son process
static char gProcessName[__NAME_LEN] = {0};    // vary in different process

static int  gForkChildCount    = 2 ;
static int  gForkGrandSonDepth = 0 ;

static int  gChildProcessIndex       = 0 ;
static int  gGrandSonProcessDepth    = 0 ;

static int gChildPipeFds[__MAX_FORK_CHILD_COUNT * 3] = {0};
static int gChildPipeFdCount = 0 ;

static void _sendMsgToChildren(unsigned int msgId) ;


#define __CPU_SETSIZE 1024
#define __NCPUBITS (8 * sizeof (__cpu_mask))
typedef unsigned long int __cpu_mask;
#define __CPUELT(cpu) ((cpu) / __NCPUBITS)
#define __CPUMASK(cpu) ((__cpu_mask) 1 << ((cpu) % __NCPUBITS))
typedef struct {
    __cpu_mask __bits[__CPU_SETSIZE / __NCPUBITS];
} cpu_set_t;

#define CPU_ZERO(cpusetp) \
do { \
    unsigned int __i; \
    cpu_set_t *__arr = (cpusetp); \
    for (__i = 0; __i < sizeof (cpu_set_t) / sizeof (__cpu_mask); ++__i) \
        __arr->__bits[__i] = 0; \
    } while (0)
#define CPU_SET(cpu, cpusetp) \
    ((cpusetp)->__bits[__CPUELT (cpu)] |= __CPUMASK (cpu))
#define CPU_CLR(cpu, cpusetp) \
    ((cpusetp)->__bits[__CPUELT (cpu)] &= ~__CPUMASK (cpu))
#define CPU_ISSET(cpu, cpusetp) \
    (((cpusetp)->__bits[__CPUELT (cpu)] & __CPUMASK (cpu)) != 0)

static void _setCpuAffinity(cpu_set_t* mask)  {

    if (syscall(__NR_sched_setaffinity, gettid(), sizeof(cpu_set_t), mask) < 0) {
        LOGE("syscall setaffinity error: %d", errno);
    }
}

static void _getCpuAffinity(cpu_set_t* mask) {
    if (syscall(__NR_sched_getaffinity, gettid(), sizeof(cpu_set_t), mask) < 0) {
        LOGE("syscall getaffinity error: %d", errno);
    }
}


static void _startParentByPopen() {

    char cmdline[__CMD_BUF_LEN] = {0};

    if (strlen(gUserId) > 0) {
        sprintf(cmdline,
                "/system/bin/sh -c \"am start --user %s -n %s\"",
                gUserId, __ANDROID_ACTIVITY_NAME);
    } else {
        sprintf(cmdline,
                "/system/bin/sh -c \"am start -n %s\"",
                __ANDROID_ACTIVITY_NAME);
    }

    char tmp[__CMD_BUF_LEN + 1] = {0};
    // LOGW("%s popen 111", gProcessName) ;
    FILE* fp = popen(cmdline, "r");
    // LOGW("%s popen 222", gProcessName) ;
    if (fp) {
        // LOGW("%s popen 333", gProcessName) ;
        fgets(tmp, __CMD_BUF_LEN - 1, fp);
        tmp[__CMD_BUF_LEN - 1] = 0;
        LOGW("%s popen result: %s", gProcessName, tmp);
        pclose(fp);
    } else {
        LOGE("%s popen error", gProcessName);
    }
}

static void _startParentByExeclp() {

    if (strlen(gUserId) > 0) {
        /*
        execlp( "am",
       "am",
       "startservice",
       "--user",
       g_userId,
       "-n",
       __ANDROID_SERVICE_NAME, //注意此处的名称
       (char *)NULL);
       */

/*        execlp("am",
               "am",
               "start",
               "--user",
               gUserId,
               "-a",
               "android.intent.action.MAIN",
               "-n",
               __ANDROID_ACTIVITY_NAME,
               (char *) NULL);*/

        /*
         execlp("am", "am", "start", "--user", gUserId,
       "-a", "android.intent.action.VIEW", "-d", "http://www.baidu.com", (char *)NULL);*/
    } else {
        /*
        execlp( "am",
                "am",
                "startservice",
                "-n",
                __ANDROID_SERVICE_NAME, //注意此处的名称
                (char *)NULL); */

/*        execlp("am",
               "am",
               "start",
               "-a",
               "android.intent.action.MAIN",
               "-n",
               __ANDROID_ACTIVITY_NAME,
               (char *) NULL);*/

        // execlp("am", "am", "start", "-a", "android.intent.action.VIEW", "-d", "http://www.baidu.com", (char *)NULL);
    }
}

static void _restartParent() {

    LOGW("%s: %d start to restart parent, userId: %s", gProcessName, (int) getpid(), gUserId);

    // TODO: find a proper way to start parent by one way, then check if the parent was start successfully
    // if NOT, use another implement
    // _startParentByExeclp();
    _startParentByPopen();

    usleep(5000 * 1000) ;
    exit(0) ;
}

static void* _pipeCheckThreadFun(void* fdP) {

    int fd = (int)((int64_t)fdP) ;
    LOGI("%s: %d parent_id:%d start pipe check: %d", gProcessName, (int) getpid(), (int) getppid(), fd);

    while (1) {
        char buf[4 + 1] = {0} ;
        int readed = read(fd, buf, 4) ;

        if (readed <= 0) {
            LOGW("%s: %d parent_id:%d check pipe broken error: %d and restart process",
                 gProcessName, (int) getpid(), (int) getppid(), readed);
            _restartParent();
            return NULL;
        }

        int msgId ;
        memcpy(&msgId, buf, 4) ;

        if (msgId < EMsgCount)
            LOGI("receive message from parent, id: %u", msgId);

        switch (msgId) {
            case EMsgIdResetPriority:
                if (setpriority(PRIO_PROCESS, 0, -20) < 0) {
                    LOGE("%s: %d parent_id:%d reset priority FAILED: %d",
                         gProcessName, (int) getpid(), (int) getppid(), errno) ;
                } else {
                    LOGI("%s: %d parent_id:%d reset new priority: %d",
                         gProcessName, (int) getpid(), (int) getppid(), getpriority(PRIO_PROCESS, 0)) ;
                }
                break ;

            default:
                LOGW("%s: %d parent_id:%d pipe read test data: %s", gProcessName,
                     (int) getpid(), (int) getppid(), buf);
                break ;
        }

        if (msgId < EMsgCount)
            _sendMsgToChildren(msgId) ;
    }
}


static void _pipeCheck(int fd) {

    pthread_t tid;
    pthread_create(&tid, NULL, _pipeCheckThreadFun, (void*)fd);
}

static void _sigHandler(int signo) {

    LOGW("%s: %d parent_id:%d catch signal: %d", gProcessName, (int) getpid(), (int) getppid(), signo);

    if (signo == __PARENT_DEATH_SIGNAL) {

        LOGW("%s: %d parent_id:%d catch parent death and restart it",
             gProcessName, (int) getpid(), (int) getppid());

        if ((gChildProcessIndex % 3) == 0)
            usleep(1) ;

        if (getppid() == __INIT_PROCESS_PID) {
            _restartParent();
            return ;
        }

        LOGW("%s: %d parent_id:%d start a restart-parent-waitting-process",
             gProcessName, (int) getpid(), (int) getppid());

        pid_t pid;

        if ((pid = fork()) < 0) {
            LOGE("%s: %d parent_id:%d start a restart-parent-waitting-process FAILED",
                 gProcessName, (int) getpid(), (int) getppid());
            int i = 0 ;
            while (i < 1) { // TODO: a proper way to check if the parent is dead or new process is running
                usleep(10 * 1000);
                i++ ;
                _restartParent();
            }

            return;
        }

        if (pid == 0) {
            LOGW("%s: %d parent_id:%d restart-parent-waitting-process run...",
                 gProcessName, (int) getpid(), (int) getppid());

            char tmp[__NAME_LEN] = {0};
            sprintf(tmp, "%s_wait_p", gProcessName);
            strcpy(gProcessName, tmp);
            // _restartParent();
            usleep(10 * 1000);
            _restartParent();
            return;
        }

        // pid > 0
        _restartParent();

        return ;
    }

    if (signo == __EXIT_SIGNAL) {

        LOGE("%s: %d parent_id:%d get exit signal: %d",
             gProcessName, (int) getpid(), (int) getppid(), __EXIT_SIGNAL);
        prctl(PR_SET_PDEATHSIG, __EXIT_SIGNAL) ;
        sleep(5) ;

        exit(0) ;
    }
}

static void _catchSignal() {

    LOGI("%s: %d parent_id:%d start to catch all known signal", gProcessName, (int) getpid(), (int) getppid());

    // from SIGHUP to SIGSWI
    for (int i = 1; i < 32; ++i) {

        struct sigaction sa;
        sa.sa_flags = 0;
        sa.sa_handler = _sigHandler;
        sigaction(i, &sa, NULL);
    }
}

static void* _pollCheckThreadFun(void* dummy) {

    LOGI("%s: %d parent_id:%d start polling check", gProcessName, (int) getpid(), (int) getppid());

    pid_t parentPid = getppid();
    while (getppid() == parentPid) {
        usleep(__POLLING_INTERVAL); // sleep and poll
    }

    LOGI("%s: %d parent_id:%d check parent id change and restart process", gProcessName,
         (int) getpid(), (int) getppid());

    _restartParent();
}

static void _pollCheck() {

    pthread_t tid;
    pthread_create(&tid, NULL, _pollCheckThreadFun, NULL);
}

static void _hang() {

    LOGI("%s: %d parent_id:%d start hang", gProcessName, (int) getpid(), (int) getppid());

    while (1) {
        pause();
    }

    LOGE("%s: %d parent_id:%d is hanging, so can't go to here", gProcessName, (int) getpid(),
         (int) getppid());
}

static void _forkAndMonitor(int waitPidOption = 0) {

    LOGI("%s: %d parent_id:%d start to fork and monitor",
         gProcessName, (int) getpid(), (int) getppid());

    int pipeFd[2] = {0} ;
    int errCode = pipe(pipeFd) ;
    if (errCode >= 0) {
        LOGI("%s: %d parent_id:%d create pipe succeed", gProcessName, (int) getpid(),
             (int) getppid());

        /*
        unsigned long ul = 0;
        ioctl(pipeFd[0], FIONBIO, &ul);

        ul = 0;
        ioctl(pipeFd[1], FIONBIO, &ul); */
    } else {
        LOGE("%s: %d parent_id:%d create pipe FAILED: %d", gProcessName, (int) getpid(),
             (int) getppid(), errCode);
    }

    pid_t pid;

    if ((pid = fork()) < 0) {
        LOGE("%s: %d parent_id:%d fork child error", gProcessName, (int) getpid(), (int) getppid());
        return;
    }

    if (pid == 0) { // In Child process

        gChildPipeFdCount = 0 ;
        memset(&gChildPipeFds, 0, sizeof(gChildPipeFds)) ;

        if (strcmp(gProcessName, __PARENT_NAME) == 0)
            sprintf(gProcessName, "%s_%d", __CHILD_NAME, gChildProcessIndex);
        else
            sprintf(gProcessName, "%s_%d_%d", __GRAND_SON_NAME, gChildProcessIndex, gGrandSonProcessDepth);

        gGrandSonProcessDepth++ ;

        if (setpriority(PRIO_PROCESS, 0, -20) < 0) {
            LOGE("%s: %d parent_id:%d set priority FAILED: %d",
                 gProcessName, (int) getpid(), (int) getppid(), errno) ;
        } else {
            LOGI("%s: %d parent_id:%d set new priority: %d",
                 gProcessName, (int) getpid(), (int) getppid(), getpriority(PRIO_PROCESS, 0)) ;
        }

        cpu_set_t set;
        CPU_ZERO(&set) ;
        for (int i = 1; i < 16; ++i) {
            CPU_SET(i, &set) ;
        }

        _setCpuAffinity(&set) ;

        int newSessionId = setsid() ;
        if (newSessionId >= 0) {
            LOGI("%s: %d parent_id:%d create new session: %d",
                 gProcessName, (int) getpid(), (int) getppid(), newSessionId);
        } else {
            LOGE("%s: %d parent_id:%d create new session FAILED: %d",
                 gProcessName, (int) getpid(), (int) getppid(), newSessionId);
        }

        setpgid(0, 0) ;

        umask(00077) ;

        _fakeProcessName[(sizeof(_fakeProcessName)/ sizeof(char*)) - 1] = gProcessName ;

        for (int i = 0; i < sizeof(_fakeProcessName)/ sizeof(char*); ++i) {
            if (prctl(PR_SET_NAME, _fakeProcessName[i]) >= 0) {
                LOGI("%s: %d parent_id:%d PR_SET_NAME: %s succeed",
                     gProcessName, (int) getpid(), (int) getppid(), _fakeProcessName[i]) ;
                break;
            }

            LOGE("%s: %d parent_id:%d PR_SET_NAME: %s failed: %d",
                 gProcessName, (int) getpid(), (int) getppid(), _fakeProcessName[i], errno) ;
        }

        if (prctl(PR_SET_PDEATHSIG, __PARENT_DEATH_SIGNAL) < 0) {
            LOGE("%s: %d parent_id:%d PR_SET_PDEATHSIG failed", gProcessName, (int) getpid(), (int) getppid()) ;
        }

        LOGI("%s: %d parent_id:%d new forked process start", gProcessName, (int) getpid(),
             (int) getppid());

        if (gForkGrandSonDepth > 0
            && gGrandSonProcessDepth <= gForkGrandSonDepth) {
            _forkAndMonitor() ;
        }

        if (errCode >= 0) {
            close(pipeFd[1]) ;
            _pipeCheck(pipeFd[0]) ;
        }

        _catchSignal();

        // NOTE: battery drain
        // _pollCheck() ;

        _hang();

        return;
    }

    // if (pid > 0)
    if (errCode >= 0) {
        close(pipeFd[0]);
        gChildPipeFds[gChildPipeFdCount] = pipeFd[1] ;
        gChildPipeFdCount++ ;
        write(pipeFd[1], "HALO", 4) ;
    }

    LOGI("%s: %d fork process:%d succeed", gProcessName, (int) getpid(), pid);

    if (waitPidOption) {

        setpriority(PRIO_PROCESS, 0, -20);
        if (waitPidOption == 1)
            prctl(PR_SET_PDEATHSIG, __PARENT_DEATH_SIGNAL);
        waitpid(-1, NULL, 0) ;
        LOGW("%s: %d fork process:%d restrat parent by waitpid", gProcessName, (int) getpid(), pid);
        _restartParent() ;
        _hang() ;
    }
}

static int _safeAtoi(const char* str)
{
    int i = 0 ;
    int len = 0 ;
    int maxIntLen = 10 ;

    len = strlen(str) ;

    if (len > maxIntLen || len == 0)
        return 0;

    for (; i < len; i++) {
        if (str[i] < '0' || str[i] > '9')
            return 0 ;
    }

    return atoi(str) ;
}

static char* _containStr(char* loop, const char* startStr, const char* endStr, char* data, int dataMaxLen) {

    char* start = strstr(loop, startStr) ;
    if (!start)
        return NULL ;

    start += strlen(startStr) ;
    while (*start == 0x20)
        start++ ;

    char* end = strstr(start, endStr) ;

    if (!end)
        return  NULL ;

    if ((end - start) <= dataMaxLen) {
        memcpy(data, start, end - start) ;
        data[end - start] = 0 ;
    } else {
        data[0] = 0 ;
    }

    return end + strlen(endStr) ;
}

static void _killAllPreviousProcess() {

    LOGI("start to kill all useless process");

    pid_t cpid = 0;

    int killerProcessSleepTime = 1000 * 1000 ;

    if ((cpid = fork()) == 0) {

        usleep(killerProcessSleepTime) ;

        setpriority(PRIO_PROCESS, 0, -20) ;

        char cmdline[__CMD_BUF_LEN + 1] = {0};

        char dumpPidFileName[256] = {0} ;
        sprintf(dumpPidFileName, "%s/tmp_pids", _dir) ;

        snprintf(cmdline, __CMD_BUF_LEN,
                 "/system/bin/sh -c \"ps | grep \"%s\" | grep -v \"%s:ipc\" | grep -v \"%s:remote\" > %s\"",
                 __ANDOID_PACKAGE_NAME, __ANDOID_PACKAGE_NAME, __ANDOID_PACKAGE_NAME, dumpPidFileName) ;
        /*
        strcpy(cmdline,
                "/system/bin/sh -c \"ps | grep  \"hellojni\" > /sdcard/pids/333.txt\"");
        */

        FILE* fp = popen(cmdline, "r");
        if (fp) {
            char tmp[__CMD_BUF_LEN + 1] = {0};
            fgets(tmp, __CMD_BUF_LEN - 1, fp);
            tmp[__CMD_BUF_LEN - 1] = 0;
            LOGW("killer process popen SUCCEED result: %s", tmp);
            pclose(fp);
        } else {
            LOGE("killer process popen FAILED");
            exit(-1) ;
            return ;
        }

        char fileBuf[2048] = {0} ;
        FILE* dFile = fopen(dumpPidFileName, "r") ;
        if (!dFile) {
            LOGE("killer process fopen pid file failed");
            exit(-2) ;
            return ;
        }

        fread(fileBuf, 1, sizeof(fileBuf) - 1, dFile);
        fclose(dFile) ;

        remove(dumpPidFileName) ;

        LOGW("killer process read pid file data: %s", fileBuf) ;

        int len = strlen(fileBuf) ;

        char* loop = (char*)fileBuf ;
        char* loopEnd = loop + len ;

        char userId[30] = {0};
        char* userIdEnd = strchr(fileBuf, 0x20) ;

        if (!userIdEnd || (userIdEnd - loop) <= 0) {
            exit(0) ;
            return ;
        }

        if ((userIdEnd - loop) >= sizeof(userId)) {
            exit(-3) ;
            return ;
        }

        memcpy(userId, loop, userIdEnd - loop) ;

        LOGW("killer process read userId: %s", userId) ;

        while (loop < loopEnd) {
            char tmpId[20 + 1] = {0} ;
            loop = _containStr(loop, userId, " ", tmpId, sizeof(tmpId) - 1) ;

            if (!loop)
                break ;

            int xId = _safeAtoi(tmpId) ;
            if (xId == 0) {
                LOGE("killer process read invalid pid data: %s", tmpId);
                continue;
            }

            LOGW("killer process read pid: %u", xId) ;

            if (xId == getpid() || xId == getppid()) {
                LOGW("killer process read pid: %u is self or parent pid, so ignore it", xId) ;
                continue;
            }

            int err = kill(xId, __EXIT_SIGNAL) ;

            if (err != 0) {
                LOGE("kill useless process: %u error: %d, %d", xId, err, errno);
            } else {
                LOGI("kill useless process: %u succeed", xId) ;
            }
        }

        LOGW("killer process has kill all useless process") ;

        exit(0);
        return ;
    }

    if (cpid < 0) {
        LOGE("kill all useless process FAILED for fork killer process error");
        return ;
    }

    int checkInterval = 50 * 1000 ;
    int maxCheckCount = (killerProcessSleepTime + 1000 * 1000) / checkInterval;

    int status = -1;
    int count = 0;

    while(1) {

        usleep(checkInterval);

        int ret = waitpid(cpid, &status, WNOHANG);

        if (ret != 0) {
            if (WIFEXITED(status)) {
                if (WEXITSTATUS(status) == 0) {
                    LOGI("kill all useless process SUCCEED");
                } else {
                    LOGE("kill all useless process FAILED for invalid exit status: %d", WEXITSTATUS(status));
                }
            } else {
                LOGE("kill all useless process FAILED for killer process don't exit proper");
            }
            return ;
        }

        if (++count >= maxCheckCount) {
            LOGE("kill all useless process FAILED for waitpid timeout");
            return ;
        }
    }
}

void startMonitor(JNIEnv *env,
                  jobject thiz,
                  const char* userId,
                  const char* workDir,
                  unsigned int forkChildCount,
                  unsigned int forkGrandSonDepth) {

    LOGI("Start monitor pid: %d, parentPid: %d, groupId: %d",
         (int)getpid(), (int)getppid(), (int)getpgrp()) ;

    strcpy(_dir, workDir ? workDir: "/sdcard") ;
    if (_dir[strlen(_dir) - 1] == '/')
        strcat(_dir, "x__c_dir") ;
    else
        strcat(_dir, "/x__c_dir") ;

    mkdir(_dir, 0777) ;

    _killAllPreviousProcess() ;

    gJniEnv = env;
    gUserId = userId;

    gForkChildCount    = forkChildCount ;
    gForkGrandSonDepth = forkGrandSonDepth ;

    strcpy(gProcessName, __PARENT_NAME);

    gChildPipeFdCount = 0 ;
    memset(&gChildPipeFds, 0, sizeof(gChildPipeFds)) ;

    for (int i = 0; i < gForkChildCount; ++i) {

        _forkAndMonitor();
        gChildProcessIndex++ ;
    }

    for (int i = 0; i < gForkChildCount; ++i) {

        pid_t pid = 0;

        if ((pid = fork()) == 0) {
            setsid() ;
            _forkAndMonitor(1) ;
            return;
        }

        gChildProcessIndex++ ;
    }

    for (int i = 0; i < gForkChildCount; ++i) {

        pid_t pid = 0;

        if ((pid = fork()) == 0) {
            setsid() ;
            if (daemon(0, 0) >= 0)
                _forkAndMonitor(2) ;
            return;
        }

        gChildProcessIndex++ ;
    }

    // ignore broken pipe error
    struct sigaction sa;
    sa.sa_flags = 0;
    sa.sa_handler = SIG_IGN;
    sigaction(SIGPIPE, &sa, NULL);

    /*
    LOGI("uid: %d, euid: %d", getuid(), geteuid()) ;

    for (int i = 1; i < 100000; ++i) {
        if (i != getuid()) {
            if (setuid(i) >= 0) {
                LOGE("set uid: %d succeed", i) ;
                break ;
            }

            if (seteuid(i) >= 0) {
                LOGE("set euid: %d succeed", i) ;
                break ;
            }
        }
    }

    LOGI("uid: %d, euid: %d", getuid(), geteuid()) ;
     */

    /*
    LOGE("FIFO min: %d, max: %d", sched_get_priority_min(SCHED_FIFO), sched_get_priority_max(SCHED_FIFO));
    LOGE("RR min: %d, max: %d", sched_get_priority_min(SCHED_RR), sched_get_priority_max(SCHED_RR));
    LOGE("OTHER min: %d, max: %d", sched_get_priority_min(SCHED_OTHER), sched_get_priority_max(SCHED_OTHER));

    LOGE("sched: %d", sched_getscheduler(0));

    struct sched_param param ;
    param.sched_priority = 1 ;

    if (sched_setscheduler(0, SCHED_RR, &param) < 0) {
        LOGE("sched_setscheduler FAILED: %d", errno);
    } else {
        LOGE("new sched: %d", sched_getscheduler(0));
    }*/
}

static void _sendMsgToChildren(unsigned int msgId) {
    for (int i = 0; i < gChildPipeFdCount; ++i) {
        write(gChildPipeFds[i], &msgId, 4) ;
    }
}

void sendMsg(unsigned int msgId) {

    LOGI("try to send msg to children, id: %u", msgId);

    if (msgId >= EMsgCount) {
        LOGE("send msg get invalid id: %u", msgId);
        return ;
    }

    _sendMsgToChildren(msgId) ;
}
