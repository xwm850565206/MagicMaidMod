package com.xwm.magicmaid.util.instruction;

public class RawInstructElement
{
    public static RawInstructElement EMPTY = new RawInstructElement("empty", null, null, null);

    /**
     * base element
     */
    public String type;
    public String name;
    public String description;
    public String resourceLocation;

    public RawInstructElement(String type, String name, String description, String resourceLocation) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.resourceLocation = resourceLocation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResourceLocation() {
        return resourceLocation;
    }

    public void setResourceLocation(String resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    @Override
    public String toString() {
        return "RawInstructElement{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", resourceLocation='" + resourceLocation + '\'' +
                '}';
    }
}
