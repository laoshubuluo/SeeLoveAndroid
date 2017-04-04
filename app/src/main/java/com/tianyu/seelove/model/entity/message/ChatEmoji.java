package com.tianyu.seelove.model.entity.message;

/**
 * @author shisheng.zhao
 * @Description: 表情实体类对象
 * @date 2015-09-01 下午17:57:25
 */
public class ChatEmoji {

    /**
     * 表情资源图片对应的ID
     */
    private int id;

    /**
     * 表情资源对应的文字描述
     */
    private String character;

    /**
     * 表情资源的文件名
     */
    private String faceName;
    /**
     * 表情资源的文件名
     */
    private String mapKey;

    /**
     * 表情资源图片对应的ID
     */
    public int getId() {
        return id;
    }

    /**
     * 表情资源图片对应的ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 表情资源对应的文字描述
     */
    public String getCharacter() {
        return character;
    }

    /**
     * 表情资源对应的文字描述
     */
    public void setCharacter(String character) {
        this.character = character;
    }

    /**
     * 表情资源的文件名
     */
    public String getFaceName() {
        return faceName;
    }

    /**
     * 表情资源的文件名
     */
    public void setFaceName(String faceName) {
        this.faceName = faceName;
    }

    public String getMapKey() {
        return mapKey;
    }

    public void setMapKey(String mapKey) {
        this.mapKey = mapKey;
    }
}
