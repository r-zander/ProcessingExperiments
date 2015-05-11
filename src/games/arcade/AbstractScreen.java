package games.arcade;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public abstract class AbstractScreen {

    private final Arcade app;

    public void activate() {};

    public abstract void draw();

}
