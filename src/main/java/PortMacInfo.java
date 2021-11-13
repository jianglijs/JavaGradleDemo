import java.util.List;

/**
 * @author：Jiangli
 * @date：2021/09/11 8:12
 */
public class PortMacInfo {
    public String mac;
    public String ip;
    public int port;
    public boolean isAlive;
    public boolean checked;

    public static class DataListModel {
        private List<PortMacInfo> dataList;

        public DataListModel(List<PortMacInfo> dataList) {
            this.dataList = dataList;
        }

        public List<PortMacInfo> getDataList() {
            return dataList;
        }


    }

    public PortMacInfo(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getMac() {
        return mac;
    }

    public String getIp() {
        return ip;
    }

    public boolean isChecked() {
        return checked;
    }

    public int getPort() {
        return port;
    }


    @Override
    public String toString() {
        return "PortMacInfo{" +
                "mac='" + mac + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", isAlive=" + isAlive +
                ", checked=" + checked +
                '}';
    }
}
