package mainPackage;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;


public class JUnitTests
{
    /**
     * Background
     */
    @Test
    public void testBackgroundConstructor()
    {
        assertNotNull( new Background( 30, 30 ) );
    }

    /**
     * Bullet
     */

    Bullet b;


    @Test
    public void testBulletConstructor()
    {
        b = new Bullet( 30, 30, 30 );
        assertNotNull( b );
    }


    @Test
    public void testBulletVerify()
    {
        b = new Bullet( 30, 30, 30 );
        b.verify( 0, 0 );
        assertEquals( b.getBounds().x, 0 );
        assertEquals( b.getBounds().y, 0 );
    }


    /**
     * Client: connection to server tested manually
     */

    /**
     * Data
     */
    public void testDataEncodeOne()
    {
        assertEquals( Data.encodeOne( new int[] { 0, 0, 0, 0, 0, 0, 0 }, 0 ), "_0,0,0,0,0,0,0,0" );
    }


    @Test
    public void testDataConvertToStock()
    {
        assertEquals( Data.convertToStock( 15 ), 3 );
        assertEquals( Data.convertToStock( 14 ), 3 );
        assertEquals( Data.convertToStock( 10 ), 2 );
        assertEquals( Data.convertToStock( 9 ), 2 );
        assertEquals( Data.convertToStock( 5 ), 1 );
        assertEquals( Data.convertToStock( 4 ), 1 );
    }


    @Test
    public void testDataConvertToHealth()
    {
        assertEquals( Data.convertToHealth( 15 ), 5 );
        assertEquals( Data.convertToHealth( 7 ), 2 );
        assertEquals( Data.convertToHealth( 3 ), 3 );
    }


    @Test
    public void testDataEncodeAll()
    {
        assertNotNull( Data.encodeAll( new ArrayList<Player>(), new ArrayList<Entity>(), new ArrayList<Bullet>() ) );
    }


    @Test
    public void testDataDecodeOne()
    {
        assertNotNull( Data.decodeOne( "_0,0,0,0,0,0,0,0" ) );
    }

    /**
     * Direction
     */

    Direction d;


    @Test
    public void testDirectionConstructor()
    {
        d = new Direction( 3 );
        assertNotNull( d );
    }


    /**
     * Entity: is an interface (methods are super simple anyway)
     */

    /**
     * GameBoard
     */

    @Test
    public void testGameBoardConstructor()
    {
        GameBoard gb = new GameBoard( 0, 0 );
        assertNotNull( gb );
    }


    @Test
    public void testGameBoardInitID()
    {
        GameBoard gb = new GameBoard( 0, 0 );
        gb.initID( 3 );
        assertEquals( 3, gb.id );
    }


    @Test
    public void testGameBoardInitPlayers()
    {
        GameBoard gb = new GameBoard( 0, 0 );
        gb.initPlayers( 1 );
        assertEquals( 1, gb.players.size() );
    }


    @Test
    public void testGameBoardReset()
    {
        GameBoard gb = new GameBoard( 0, 0 );
        gb.reset();
        assertFalse( gb.gameOver );
        assertFalse( gb.beginAgain );
    }


    /**
     * Gun
     */
    public void testGunConstructor()
    {
        Gun g = new Gun( 0, 0, 0, 0 );
        assertNotNull( g );
    }


    @Test
    public void testGunAddBullet()
    {
        Gun g = new Gun( 0, 0, 0, 0 );
        assertNull( g.addBullet() );
    }


    /**
     * Lobby
     */

    @Test
    public void testLobbyConstructor()
    {
    		assertNotNull(new Lobby(1200, 800));
    }


    /**
     * MainFrame
     */

    @Test
    public void testMainFrameConstructor()
    {
		assertNotNull(new MainFrame());
    }


    /**
     * NameGetter
     */

    @Test
    public void testNameGetterConstructor()
    {
		assertNotNull(new NameGetter(1200,800));
    }


    /**
     * Player
     */

    @Test
    public void testPlayerConstructor()
    {
		Player p = new Player(100, 100, true, 0, 0, 0);
		assertNotNull(p);
		assertEquals(Integer.parseInt(p.health), 15);
		assertEquals(p.id, 0);
		assertNotNull( p.shell );
		assertNotNull( p.gun );
		assertEquals( p.spawnX, 0);
		assertEquals( p.spawnY, 0);
		assertNotNull( p.sbar );
    }


    @Test
    public void testPlayerNewCoordinates()
    {
		Player p = new Player(100, 100, true, 0, 0, 0);
		assertNotNull(p.newCoordinates());
    }


    @Test
    public void testPlayerTakeDamage()
    {
		Player p = new Player(100, 100, true, 0, 0, 0);
		p.takeDamage();
		assertEquals(Integer.parseInt(p.health), 14);
    }


    @Test
    public void testPlayerRespawn()
    {
		Player p = new Player(100, 100, true, 0, 0, 0);
		p.respawn();
		assertEquals(p.spawnX, 0);
		assertEquals( p.spawnY, 0);
    }


    @Test
    public void testPlayerGameOver()
    {
		Player p = new Player(100, 100, true, 0, 0, 0);
		p.gameOver(true);
		assertEquals(p.shell.bounds.x, -200);
		p.gameOver(false);
		assertEquals(p.shell.bounds.x, -100);

    }


    @Test
    public void testPlayerReset()
    {
		Player p = new Player(100, 100, true, 0, 0, 0);
		p.reset();
		assertEquals(p.spawnX, 0);
		assertEquals( p.spawnY, 0);
		assertEquals(Integer.parseInt(p.health), 15);
    }


    /**
     * Server: must test server code manually
     */

    /**
     * ServerBoard
     */

    @Test
    public void testServerBoardConstructor()
    {
		assertNotNull( new ServerBoard(1200,800) );
    }


    @Test
    public void testServerBoardGetPlayerCount()
    {
		ServerBoard sb = new ServerBoard(1200,800);
		sb.addPlayer();
		assertEquals(1, sb.getPlayerCount());
    }


    @Test
    public void testServerBoardAddPlayer()
    {
		ServerBoard sb = new ServerBoard(1200,800);
		sb.addPlayer();
		assertEquals(1, sb.getPlayerCount());
    }


    @Test
    public void testServerBoardReset()
    {
		ServerBoard sb = new ServerBoard(1200,800);
		sb.reset();
		assertNotNull(sb.players);
    }


    /**
     * ServerThread: Server code needs to be manually tested
     */

    /**
     * Shell
     */

    @Test
    public void testShellConstructor()
    {
		assertNotNull( new Shell(0,0,100) );
    }


    /**
     * StatusBar
     */

    @Test
    public void testStatusBarConstructor()
    {
		StatusBar sb = new StatusBar(true, 0,0,15,0);
		assertNotNull(sb);
    }


    /**
     * Wall
     */

    @Test
    public void testWallConstructor()
    {
		assertNotNull(new Wall(100,100,0,0));
    }
}
