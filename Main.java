package a;

import c.Task;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import me.onebone.economyapi.EconomyAPI;

public class Main extends PluginBase implements Listener{
	private Config config;	
	@Override
	public void onEnable(){
		this.getDataFolder().mkdir();
		this.config=new Config(getDataFolder()+"/config.yml",Config.YAML);
		if(config.exists("flycost")){
			this.saveResource("config.yml");
		}else if(!(config.exists("flycost"))){
			config.set("flycost",1000);
		}
		this.getServer().getPluginManager().registerEvents(this, this);
		this.getServer().getLogger().info("PayForFly Enabled!-");
		config.save();
	}
	@Override
	public void onDisable(){
		config.save();
	}
	 @SuppressWarnings("deprecation")
	@Override
     public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		 if(command.getName().equals("fly")){
			 if(!(sender instanceof Player)){
				 sender.sendMessage("플레이어만 사용가능한 명령어입니다.");
				 return false;
			 }else{
				 int min=0;
				 try {
					min=Integer.parseInt(args[0]);
				} catch (NumberFormatException e) {
					sender.sendMessage("숫자만을 입력해주세요.");
					return false;
					// TODO: handle exception
					}
				Player player = (Player)sender;
				double flycost = Double.parseDouble(config.get("flycost").toString());
				 if(args.length==0){
					 if(EconomyAPI.getInstance().myMoney(player)>=flycost){
							player.setAllowFlight(true);
							EconomyAPI.getInstance().reduceMoney(player,flycost);
							player.sendMessage("플라이가 활성화 되었습니다(-"+flycost+")");
							this.getServer().getScheduler().scheduleDelayedTask(new Task(this,player),20*60*min);
							return true;
							
						}else if(EconomyAPI.getInstance().myMoney(player)<flycost){
							player.sendMessage("돈이 부족합니다   필요금액: "+flycost);
							return false;
						}
				 }
				 else if(args.length==1){
					 if(EconomyAPI.getInstance().myMoney(player)>=flycost*min){
							player.setAllowFlight(true);
							EconomyAPI.getInstance().reduceMoney(player,flycost*min);
							player.sendMessage("플라이가 활성화 되었습니다(-"+flycost*min+")");
							this.getServer().getScheduler().scheduleDelayedTask(new Task(this,player),20*60*min);
							return true;
							
						}else if(EconomyAPI.getInstance().myMoney(player)<flycost*min){
							player.sendMessage("돈이 부족합니다   필요금액: "+flycost*min);
							return false;
						}
					}
			 }
		 }
		return false;
		 
	 }
	}


