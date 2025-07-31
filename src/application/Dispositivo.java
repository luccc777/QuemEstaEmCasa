package application;

public class Dispositivo {
    private String nome;
    private String ip;
    private String mac;
    private String descricao;

    public Dispositivo(String nome, String ip, String mac) {
        this.nome = nome;
        this.ip = ip;
        this.mac = mac;
    }

    public String getNome() {
        return nome;
    }

    public String getIp() {
        return ip;
    }

    public String getMac() {
        return mac;
    }
    
    public String getDescricao() {
    	return descricao;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
    
    public void setDescricao(String descricao) {
    	this.descricao=descricao;
    }
}
