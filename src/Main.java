import java.util.Scanner;

import fr.insa.ot3.communication.Client;
import fr.insa.ot3.communication.Server;
import fr.insa.ot3.communication.message.GameListRequest;
import fr.insa.ot3.communication.message.NewGame;
import fr.insa.ot3.utils.Color;


public class Main {

	public static void main(String[] args) throws InterruptedException {
		int port = 8080;
		
		for(int i = 0; i < args.length; ++i) {

			if(args[i].equals("--help") || args[i].equals("-h"))
			{
				showHelp();
				return;
			}
			else if(args[i].equals("--port") || args[i].equals("-p"))
			{
				if(i < args.length - 1)
				{
					port = Integer.parseInt(args[i+1]);
					i++;
				}
			}
		}

		Server s = new Server(port);
		s.start();
		System.out.println(Color.CYAN + " Starting server..." + Color.RESET);
		
		Thread.sleep(100);
		
		Client fakeClient = new Client("localhost", port);
		fakeClient.sendMessage(new NewGame("coin", false, 15, 1, 5, "Avion", "player1"));
		fakeClient.sendMessage(new NewGame("bam", false, 15, 1, 5, "Moteur", "player1"));
		fakeClient.sendMessage(new NewGame("bamm", false, 15, 1, 5, "Moteur", "player2"));
		
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		while(true)
		{
			System.out.print("> ");
			
			String cmd = sc.nextLine();
			String cmds[] = cmd.split(" ");
			if(cmds.length <= 0)
				continue;
			
			
			if(cmds[0].equals("stop"))
			{
				System.out.println(Color.CYAN + " Stopping server..." + Color.RESET);
				fakeClient.disconnect();
				s.stop();
				Thread.sleep(100);
				return;
			}
			else if(cmds[0].equals("listGames"))
				if(cmds.length > 1)
				{
					String playerID = cmds[1];
					fakeClient.sendMessage(new GameListRequest(playerID));
				}
				else
					fakeClient.sendMessage(new GameListRequest());
			
			else if(cmd.equals(""))
				continue;
			else
				showCmdHelp();
		}
	}

	
	private static void showHelp()
	{
		System.out.println("-h\t--help\tAffiche l'aide");
		System.out.println("-p\t--port\tSélectionne le port d'écoute (défaut 8080)");
	}
	
	private static void showCmdHelp()
	{
		System.out.println("help					Affiche l'aide");
		System.out.println("stop					Arrête le serveur");
		System.out.println("listGames				Affiche la liste des parties en cours");
		System.out.println("listGames <playerID>	Affiche la liste des parties en cours du joueur <playerID>");
	}
}
