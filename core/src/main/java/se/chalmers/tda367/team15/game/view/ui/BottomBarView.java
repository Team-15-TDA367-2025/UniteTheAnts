package se.chalmers.tda367.team15.game.view.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import se.chalmers.tda367.team15.game.model.pheromones.PheromoneType;

public class BottomBarView {
    private final UiFactory uiFactory;

    private final Table barTable;
    private final Table expandButtonTable;

    private PheromoneSelectionListener pheromoneListener;
    private ButtonGroup<TextButton> pheromoneButtonGroup;

    public BottomBarView(UiFactory uiFactory) {
        this.uiFactory = uiFactory;

        barTable = new Table();
        barTable.setBackground(uiFactory.getPanelBackground());
        // barTable.pad(UiTheme.PADDING_MEDIUM);

        buildBarContents();

        expandButtonTable = createExpandButton();
        expandButtonTable.setVisible(false);
    }

    public void addToStage(Stage stage) {
        Table container = new Table();
        container.setFillParent(true);
        container.bottom();
        container.add(barTable).center().width(UiTheme.BOTTOM_BAR_WIDTH).height(UiTheme.BOTTOM_BAR_HEIGHT)
                .pad(UiTheme.PADDING_MEDIUM);

        stage.addActor(container);
        stage.addActor(expandButtonTable);

        updateExpandPosition(stage.getViewport().getWorldWidth());
    }

    public void updateLayout(float width, float height) {
        updateExpandPosition(width);
    }

    public void setPheromoneSelectionListener(PheromoneSelectionListener listener) {
        this.pheromoneListener = listener;
    }

    private void buildBarContents() {
        HorizontalGroup pheromoneGroup = createPheromoneButtonGroup();
        HorizontalGroup otherGroup = createOtherButtonGroup();

        ImageButton minimizeBtn = uiFactory.createImageButton("BottomBar/minimize", () -> setMinimizedBar(true));

        barTable.left();
        barTable.add(pheromoneGroup).left();
        barTable.add(otherGroup).left().padLeft(UiTheme.PADDING_XXLARGE);
        barTable.add().expandX();
        barTable.add(minimizeBtn).right().size(UiTheme.ICON_SIZE_MEDIUM);
    }

    private HorizontalGroup createPheromoneButtonGroup() {
        String[] labels = { "Gather", "Attack", "Explore", "Erase" };
        PheromoneType[] types = { PheromoneType.GATHER, PheromoneType.ATTACK, PheromoneType.EXPLORE, null };

        HorizontalGroup group = new HorizontalGroup();
        group.space(UiTheme.BUTTON_SPACING);

        pheromoneButtonGroup = new ButtonGroup<>();
        pheromoneButtonGroup.setMinCheckCount(1);
        pheromoneButtonGroup.setMaxCheckCount(1);
        pheromoneButtonGroup.setUncheckLast(true);

        for (int i = 0; i < labels.length; i++) {
            final PheromoneType type = types[i];

            TextButton btn = uiFactory.createToggleTextButton(labels[i]);
            btn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (pheromoneListener != null) {
                        pheromoneListener.onPheromoneSelected(type);
                    }
                }
            });

            pheromoneButtonGroup.add(btn);
            group.addActor(btn);
        }

        pheromoneButtonGroup.getButtons().first().setChecked(true);
        return group;
    }

    private HorizontalGroup createOtherButtonGroup() {
        HorizontalGroup group = new HorizontalGroup();
        group.space(UiTheme.BUTTON_SPACING);

        String[] labels = { "Button 5", "Button 6" };

        for (int i = 0; i < labels.length; i++) {
            TextButton btn = uiFactory.createTextButton(labels[i], null);
            group.addActor(btn);
        }

        return group;
    }

    private Table createExpandButton() {
        ImageButton expandBtn = uiFactory.createImageButton("BottomBar/expand", () -> setMinimizedBar(false));
        Label lbl = new Label("expand",
                uiFactory.createLabelStyle(UiTheme.FONT_SCALE_DEFAULT, com.badlogic.gdx.graphics.Color.WHITE));

        Table t = new Table();
        t.add(expandBtn).size(UiTheme.ICON_SIZE_MEDIUM);
        t.row();
        t.add(lbl).padTop(UiTheme.PADDING_TINY);

        t.setVisible(false);
        return t;
    }

    private void setMinimizedBar(boolean minimized) {
        barTable.setVisible(!minimized);
        expandButtonTable.setVisible(minimized);
        if (minimized) {
            expandButtonTable.toFront();
        }
    }

    private void updateExpandPosition(float worldWidth) {
        float centerX = worldWidth / 2f;
        float rightEdge = centerX + (UiTheme.BOTTOM_BAR_WIDTH / 2f);

        float x = rightEdge - 50f;
        float y = UiTheme.PADDING_MEDIUM;
        expandButtonTable.setPosition(x, y, Align.bottomLeft);
    }
}
