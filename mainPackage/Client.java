package mainPackage;

import java.awt.event.InputEvent;
import java.io.*;
import java.net.*;
import java.util.ArrayList;


/**
 * Client provides GameBoard with the server information and stores names It is
 * the first thing run on the client-side
 * 
 * @author Aaron Chen
 * @version May 26, 2016
 * @author Period: 3
 * @author Assignment: AdventuresOfKidco
 *
 * @author Sources: None
 */
public class Client
{
//     static String hostName = "LHS-STAFF-B464Q72.lhs.win.fuhsd.org";
    // static String hostName = "LHS-LAB605-34";
//      static String hostName = "Aarons-MacBook-Air.local";
     //static String hostName = "Richards-MacBook-Pro.local";
	static String hostName = "LHS-LAB608-32";
    static int portNumber = 8120;

    static private PrintWriter out;

    static private BufferedReader in;

    static public MainFrame mainFrame;

    static ArrayList<String> names;

    static String myName;


    /**
     * Constructor that creates names and a mainFrame
     * 
     * @param args
     *            not used
     * @throws IOException
     *             incase it fails to connect to server
     */
    public static void main( String[] args ) throws IOException
    {
        names = new ArrayList<String>();
        mainFrame = new MainFrame();
    }


    /**
     * init's the connection
     * 
     * @return if connection is made or not
     */
    public static boolean init()
    {
        try
        {
            Socket echoSocket = new Socket( hostName, portNumber );
            echoSocket.setTcpNoDelay( true );
            out = new PrintWriter( echoSocket.getOutputStream(), true );
            in = new BufferedReader( new InputStreamReader( echoSocket.getInputStream() ) );
            return true;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * waits for everybody before starting the game then calls StartGame once
     * all is ready
     */
    public static void waitForEveryone()
    {
        try
        {
            String input;
            while ( ( input = in.readLine() ) != null )
            {
                if ( input.equals( "start game" ) )
                {
                    input = in.readLine();
                    int playerID = Integer.parseInt( input );
                    input = in.readLine();
                    int playerCount = Integer.parseInt( input );
                    mainFrame.gameBoard.initID( playerID );
                    mainFrame.gameBoard.initPlayers( playerCount );

                    out.println( Client.myName );

                    while ( ( input = in.readLine() ) != null && !input.equals( "done" ) )
                    {
                        Client.names.add( input );
                    }
                    Client.mainFrame.endLobbyStartGame();

                    startGameLoop();
                }
                else if ( input.equals( "waiting for players" ) )
                {
                    out.println( "in lobby" );
                }
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }


    /**
     * starts the game and leads to startGameLoop
     */
    public static void startGame()
    {
        try
        {
            out.println( "start the game" );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }


    /**
     * This repaints the screen to what the server outputs This also determines
     * how fast / slow game speed and fps is
     * 
     * @throws IOException
     *             incase of error during reading or writing
     */
    public static void startGameLoop() throws IOException
    {
        long initialTime = System.nanoTime();
        final double timeU = 1000000000 / 60;
        final double timeF = 1000000000 / 60;
        double deltaU = 0, deltaF = 0;

        while ( true )
        {
            long currentTime = System.nanoTime();
            deltaU += ( currentTime - initialTime ) / timeU;
            deltaF += ( currentTime - initialTime ) / timeF;

            if ( deltaU >= 1 )
            {
                String input = in.readLine();
                String output = mainFrame.gameBoard.update( input );
                if ( input.equals( "restart" ) )
                {
                    input = in.readLine();
                    mainFrame.gameBoard.reset();
                    output = mainFrame.gameBoard.update( input );
                }
                else if ( input.equals( "no" ) )
                {
                    output = "no";
                }

                if ( mainFrame.gameBoard.beginAgain )
                {
                    output = "beginAgain";
                }
                out.println( output );
                deltaU--;
            }
            if ( deltaF >= 1 )
            {
                mainFrame.gameBoard.paintImmediately( 0, 0, mainFrame.getWidth(), mainFrame.getHeight() );
                deltaF--;
            }
            initialTime = currentTime;
        }
    }
}