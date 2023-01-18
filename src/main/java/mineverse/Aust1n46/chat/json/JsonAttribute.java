package mineverse.Aust1n46.chat.json;

import mineverse.Aust1n46.chat.ClickAction;

import java.util.List;

public class JsonAttribute {
    private String name;
    private List<String> hoverText;
    private ClickAction clickAction;
    private String clickText;

    public JsonAttribute(String name, List<String> hoverText, ClickAction clickAction, String clickText) {
        this.name = name;
        this.hoverText = hoverText;
        this.clickAction = clickAction;
        this.clickText = clickText;
    }

    public String getName() {
        return name;
    }

    public List<String> getHoverText() {
        return hoverText;
    }

    public ClickAction getClickAction() {
        return clickAction;
    }

    public String getClickText() {
        return clickText;
    }
}
