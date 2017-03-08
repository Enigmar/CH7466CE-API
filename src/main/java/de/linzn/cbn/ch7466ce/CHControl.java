package de.linzn.cbn.ch7466ce;

public class CHControl {
	private static CHControl instance;

	private CBNApi cbnApi;

	public static void main(String[] args) {
		instance = new CHControl(args);

	}

	public CHControl(String[] args) {
		String cbnIP = "192.168.0.1";
		String cbnUsername = "admin";
		String cbnPassword = "password";

		if (args.length >= 4) {
			cbnPassword = args[3];
			cbnUsername = args[2];
			cbnIP = args[1];
		} else if (args.length == 3) {
			cbnUsername = args[2];
			cbnIP = args[1];
		}
		if (args.length == 2) {
			cbnIP = args[1];
		}
		if (args.length < 1) {
			System.out.println("Argumente: (IP-Adresse) (CBNUser) (CBNPassword)");
			return;
		}

		String command = args[0];

		this.cbnApi = new CBNApi(cbnIP, cbnUsername, cbnPassword);

		switch (command) {

		case "restart":
			this.cbnApi.restartCBN();

		}

	}

	public static CHControl getInstance() {
		return instance;
	}

}
