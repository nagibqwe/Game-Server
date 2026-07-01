package game.core.net.Config;

public class BI4399Config {
	private String bi4399Dir;
	private String bi4399Host;
	private String bi4399Port;
	private String bi4399Protocol;
	private int    bi4399OpenNet;
	private int    bi4399Sql;

	public BI4399Config() {
		setBi4399Dir("/bi");
		setBi4399OpenNet(0);
		setBi4399Host("msg.4399sy.com");
		setBi4399Port("5935");
		setBi4399Protocol("UDP");
		setBi4399Sql(0);
	}

	public BI4399Config(String bi4399Dir, int bi4399OpenNet, String bi4399Host, String bi4399Port, String bi4399Protocol, int bi4399Sql) {
		setBi4399Dir(bi4399Dir);
		setBi4399OpenNet(bi4399OpenNet);
		setBi4399Host(bi4399Host);
		setBi4399Port(bi4399Port);
		setBi4399Protocol(bi4399Protocol);
		setBi4399Sql(bi4399Sql);
	}

	public String getBi4399Dir() {
		return bi4399Dir;
	}

	public void setBi4399Dir(String bi4399Dir) {
		this.bi4399Dir = bi4399Dir;
	}

	public String getBi4399Host() {
		return bi4399Host;
	}

	public void setBi4399Host(String bi4399Host) {
		this.bi4399Host = bi4399Host;
	}

	public String getBi4399Port() {
		return bi4399Port;
	}

	public void setBi4399Port(String bi4399Port) {
		this.bi4399Port = bi4399Port;
	}

	public String getBi4399Protocol() {
		return bi4399Protocol;
	}

	public void setBi4399Protocol(String bi4399Protocol) {
		this.bi4399Protocol = bi4399Protocol;
	}

	public int getBi4399OpenNet() {
		return bi4399OpenNet;
	}

	public void setBi4399OpenNet(int bi4399OpenNet) {
		this.bi4399OpenNet = bi4399OpenNet;
	}

	public int getBi4399Sql() {
		return bi4399Sql;
	}

	public void setBi4399Sql(int bi4399Sql) {
		this.bi4399Sql = bi4399Sql;
	}

	public boolean isOpen4399SqlLog() {
		return this.bi4399Sql == 1;
	}
	@Override
	public String toString() {
		return "BI4399Config{" +
				"bi4399Dir='" + bi4399Dir + '\'' +
				", bi4399Host='" + bi4399Host + '\'' +
				", bi4399Port='" + bi4399Port + '\'' +
				", bi4399Protocol='" + bi4399Protocol + '\'' +
				", bi4399OpenNet=" + bi4399OpenNet +
				", bi4399Sql=" + bi4399Sql +
				'}';
	}
}
