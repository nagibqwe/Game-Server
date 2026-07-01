package game.core.net.Config;

/**
 * @explain: desc
 * @time Created on 2020/3/17 10:09.
 * @author: tc
 */
public class BIConfig {
	private String biBakDir;
	/**
	 * 是否开启网络日志，1开启/0关闭
	 */
	private int openNet;
	private String sysLogHost;
	private String sysLogPort;
	private String sysLogProtocol;

	public BIConfig() {
		setBiBakDir("/bi");
		setOpenNet(0);
		setSysLogHost("localhost");
		setSysLogPort("6789");
		setSysLogProtocol("TCP");
	}

	public BIConfig(String biBakDir, int openNet, String sysLogHost, String sysLogPort, String sysLogProtocol) {
		setBiBakDir(biBakDir);
		setOpenNet(openNet);
		setSysLogHost(sysLogHost);
		setSysLogPort(sysLogPort);
		setSysLogProtocol(sysLogProtocol);
	}

	public String getBiBakDir() {
		return biBakDir;
	}

	public void setBiBakDir(String biBakDir) {
		this.biBakDir = biBakDir.trim().replace("\\", "/");
	}

	public int getOpenNet() {
		return openNet;
	}

	public void setOpenNet(int openNet) {
		this.openNet = openNet;
	}

	public String getSysLogHost() {
		return sysLogHost;
	}

	public void setSysLogHost(String sysLogHost) {
		this.sysLogHost = sysLogHost;
	}

	public String getSysLogPort() {
		return sysLogPort;
	}

	public void setSysLogPort(String sysLogPort) {
		this.sysLogPort = sysLogPort;
	}

	public String getSysLogProtocol() {
		return sysLogProtocol;
	}

	public void setSysLogProtocol(String sysLogProtocol) {
		this.sysLogProtocol = sysLogProtocol;
	}




	@Override
	public String toString() {
		return "BIConfig{" +
				"biBakDir='" + biBakDir + '\'' +
				", openNet=" + openNet +
				", sysLogHost='" + sysLogHost + '\'' +
				", sysLogPort='" + sysLogPort + '\'' +
				", sysLogProtocol='" + sysLogProtocol + '\'' +
				'}';
	}
}
