package mainPackage;

import java.net.*;
import java.io.*;


/**
 * serves to act as the communications from server to a specific client
 * 
 * @author Aaron Chen
 * @version May 26, 2016
 * @author Period: 3
 * @author Assignment: AdventuresOfKidco
 *
 * @author Sources: None
 */
public class ServerThread extends Thread
{

    private Socket socket = null;

    private int clientID;


    /**
     * constructor for the serverThread
     * 
     * @param socket
     *            socket of thread
     * @param id
     *            of the client it is talking to
     */
    public ServerThread( Socket socket, int id )
    {
        super( "ServerThread" );
        this.socket = socket;
        clientID = id;
    }


    /**
     * the run method where it actually does the communications
     */
    @Override
    public void run()
    {
        try (PrintWriter out = new PrintWriter( socket.getOutputStream(), true );
                        BufferedReader in = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );)
        {

            out.println( "waiting for players" );
            String input;
            while ( ( input = in.readLine() ) != null )
            {
                if ( clientID == 0 )
                    Server.p1ready = true;
                else
                    Server.p2ready = true;
                if ( input.equals( "start the game" ) || Server.startGame )
                {
                    Server.startGame = true;
                    out.println( "start game" );
                    break;
                }
                else
                {
                    out.println( "waiting for players" );
                }
            }

            out.println( clientID );
            out.println( Server.serverBoard.getPlayerCount() );

            while ( ( input = in.readLine() ) != null )
            {
                if ( input.equals( "in lobby" ) )
                {
                    continue;
                }
                else
                {
                    break;
                }
            }

            if ( Server.serverBoard.names == null )
            {
                Server.serverBoard.names = new String[Server.serverBoard.getPlayerCount()];
            }
            Server.serverBoard.names[clientID] = input;
            Server.serverBoard.connections++;

            while ( Server.serverBoard.connections != 2 )
            {
            	
            }

            for ( String s : Server.serverBoard.names )
            {
                out.println( s );
            }
            out.println( "done" );

            String inputLine;
            out.println( Server.serverBoard
                .update( Data.encodeOne( new int[] { 0, 0, 0, 0, -1, 0, 15, 400 }, clientID ), clientID ) );
            while ( ( inputLine = in.readLine() ) != null )
            {
                String output = "";

                if ( Server.beginAgain || inputLine.equals( "beginAgain" ) )
                {
                    output = "restart";
                    Server.beginAgain = !Server.beginAgain;
                    Server.serverBoard.reset();
                    ServerBoard sb = Server.serverBoard;
                    out.println( output );
                    out.println( Data.encodeAll( sb.players, sb.environment, sb.bullets ) );
                }
                else if ( inputLine.equals( "no" ) )
                {
                    output = "no";
                    out.println( output );
                }
                else
                {
                    output = Server.serverBoard.update( inputLine, clientID );
                    out.println( output );
                }
            }

            socket.close();
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }
}