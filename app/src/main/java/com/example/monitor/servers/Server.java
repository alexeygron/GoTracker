package com.example.monitor.servers;

public class Server {
	
	protected String ipAddr;
	protected String srvName;
	protected String numPlayers;
	protected String maxPlayers;
	protected String srvMap;
	protected String srvGame;
	protected String srvTags;
	
	public Server(){
		
		
	}
	
	public Server(String ip){
		ipAddr = ip;
	}
	
	public String getIpAddr () {
		
		return ipAddr;
	}
	
	public void setSrvName (String name) {

		srvName = name;
	}
	
	public String getSrvName () {

		return srvName;
	}
	
	public String getGame () {

		return srvGame;
	}
	
	public String getTags () {

		return srvTags;
	}
	
	public void setTags (String tags) {

		srvTags = tags;
	}
	
	public void setGame (String game) {

		srvGame = game;
	}
	
	
	public void setNumPlayers (String num) {

		numPlayers = num;
	}
	
	public void setMaxPlayers (String max) {

		maxPlayers = max;
	}
	
	public String getMaxPlayers () {

		return maxPlayers;
	}
	
	public String getNumPlayers () {

		return numPlayers;
	}
	
	public void setMap (String mapN) {

		srvMap = mapN;
	}
	
	public String getMap () {

		return srvMap;
	}

	@Override
	public String toString(){
		return "IpAddr " + ipAddr;
	}
}
