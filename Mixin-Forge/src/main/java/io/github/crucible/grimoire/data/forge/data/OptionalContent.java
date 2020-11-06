package io.github.crucible.grimoire.data.forge.data;

public class OptionalContent <T>{

    private boolean hasContent = false;
    private T content;

    public void setContent(T content) {
        this.content = content;
        this.hasContent = true;
    }

    public T getContent() {
        return content;
    }

    public boolean hasContent() {
        return hasContent;
    }
}
