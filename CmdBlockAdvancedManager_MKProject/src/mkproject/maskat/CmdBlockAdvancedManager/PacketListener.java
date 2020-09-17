//package mkproject.maskat.CmdBlockAdvancedManager;
//
//import org.inventivetalent.packetlistener.handler.PacketHandler;
//import org.inventivetalent.packetlistener.handler.ReceivedPacket;
//import org.inventivetalent.packetlistener.handler.SentPacket;
//
//@SuppressWarnings("deprecation")
//public class PacketListener extends PacketHandler {
//
//	@Override
//	public void onReceive(ReceivedPacket e) {
//		if(e.getPacketName().equals("PacketPlayInSetCommandBlock"))
//		{
//			for(int i=0;i<20;i++)
//			{
//				try {
//					Plugin.getPlugin().getLogger().info(i+": "+e.getPacketValue(i).toString());
//				}catch(Exception ex){}
//			}
//		}
//	}
//
//	@Override
//	public void onSend(SentPacket e) {
//
//	}
//
//}
