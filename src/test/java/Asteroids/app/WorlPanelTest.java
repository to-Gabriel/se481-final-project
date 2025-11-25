package Asteroids.app;

import Asteroids.app.entity.Entity;
import Asteroids.app.entity.Player;
import Asteroids.app.util.Vector2;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class WorlPanelTest {
    @Mock private Game mockGame;
    @Mock private Graphics2D mockG2d;
    @Mock private Entity mockEntity;
    @Mock private Player mockPlayer;

    private WorldPanel worldPanel;
    private List<Entity> entityList;

    @BeforeEach
    void setUp() {
        // Initializes fields annoated with Mockito annotations
        MockitoAnnotations.openMocks(this);

        // Setup the Game Mock dependencies
        entityList = new ArrayList<>();
        when(mockGame.getEntities()).thenReturn(entityList);
        // when(mockGame.getPlayer()).thenReturn(mockPlayer);

        // Setup Graphics Mock prevent scratchGraphics null error
        when(mockG2d.create()).thenReturn(mockG2d);

        // 3. Initialize the Panel
        worldPanel = new WorldPanel(mockGame);
    }

    /**
     * Test drawing an entity
     */
    @Test
    void testPaintComponent_DrawsEntities() {
        // Add one entity to the world
        entityList.add(mockEntity);
        // Setup entity properties so it doesn't trigger wrapping logic
        when(mockEntity.getPosition()).thenReturn(new Vector2(100, 100)); 
        when(mockEntity.getCollisionRadius()).thenReturn(10.0);
        when(mockEntity.getRotation()).thenReturn(0.0);

        // Manually call paintComponent
        worldPanel.paintComponent(mockG2d);

        // Verify the entity was asked to draw itself
        // Checks that 'draw' was called on the mockEntity one time
        verify(mockEntity, times(1)).draw(mockG2d, mockGame);
    }

    /**
     * Test screen wrapping
     * If an entity is partially off-screen (x < radius), it should be drawn twice.
     * Once at the original spot, and once wrapped around.
     */
    @Test
    void testPaintComponent_WrapsEntityDrawing() {
        entityList.add(mockEntity);
        
        // Position is 5, but Radius is 10.
        // This triggers the condition: (pos.x < radius)
        when(mockEntity.getPosition()).thenReturn(new Vector2(5, 100));
        when(mockEntity.getCollisionRadius()).thenReturn(10.0);
        when(mockEntity.getRotation()).thenReturn(0.0);

        worldPanel.paintComponent(mockG2d);

        // Entity draw should be called twice
        // 1. At the original position
        // 2. At the wrapped position (calculated inside WorldPanel)
        verify(mockEntity, times(2)).draw(mockG2d, mockGame);
        
        // Verify translated graphics context for the wrap
        // The World size is 550. If x=5, wrap should add 550.
        verify(mockG2d).translate(eq(5.0 + WorldPanel.WORLD_SIZE), eq(100.0));
    }
}
