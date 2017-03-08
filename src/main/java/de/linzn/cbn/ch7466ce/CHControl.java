package de.linzn.cbn.ch7466ce;

public class CHControl {
	private static  CHControl instance;
	
	private String cbnIP = "192.168.0.1";
	private String cbnUsername = "admin";
	private String cbnPassword = "password";
	
	private CBNApi cbnApi;
	

	public static void main(String[] args) {
		instance = new CHControl(args);

	}
	
	public CHControl(String[] args){
		cbnApi = new CBNApi();
	
		
		if (args.length >= 4){
			this.cbnPassword = args[3];
			this.cbnUsername = args[2];
			this.cbnIP = args[1];
		} else if (args.length == 3){
			this.cbnUsername = args[2];
			this.cbnIP = args[1];
		} if (args.length == 2){
			this.cbnIP = args[1];
		} if (args.length < 1){
			System.out.println("Argumente: (IP-Adresse) (CBNUser) (CBNPassword)");
			return;
		}
		
		String command = args[0];
		
		switch (command) {
		
		case "restart":
			this.cbnApi.restartCBN(this.cbnIP, this.cbnUsername, this.cbnPassword);
		
		}
		
		
	}

	
	
	

	public static CHControl getInstance(){
		return instance;
	}
	
}
