package application;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.*;

public class RedeScanner {

	public static List<Dispositivo> escanearRede() {
	    List<Dispositivo> dispositivos = new ArrayList<>();

	    try {
	        String localIP = null;
	        String subnet = null;

	        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
	        while (interfaces.hasMoreElements()) {
	            NetworkInterface ni = interfaces.nextElement();
	            if (!ni.isUp() || ni.isLoopback() || ni.isVirtual()) continue;

	            for (InterfaceAddress ia : ni.getInterfaceAddresses()) {
	                InetAddress addr = ia.getAddress();
	                if (addr instanceof Inet4Address) {
	                    localIP = addr.getHostAddress();
	                    subnet = localIP.substring(0, localIP.lastIndexOf('.') + 1);
	                    break;
	                }
	            }

	            if (localIP != null) break;
	        }

	        if (subnet == null) {
	            System.out.println("Não foi possível detectar a sub-rede.");
	            return dispositivos;
	        }

	        NetworkInterface ni = NetworkInterface.getByInetAddress(InetAddress.getByName(localIP));
	        String macLocal = "";
	        if (ni != null) {
	            byte[] macBytes = ni.getHardwareAddress();
	            if (macBytes != null) {
	                StringBuilder sb = new StringBuilder();
	                for (byte b : macBytes) {
	                    sb.append(String.format("%02X:", b));
	                }
	                if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
	                macLocal = sb.toString();
	            }
	        }
	        dispositivos.add(new Dispositivo("Este dispositivo", localIP, macLocal));

	        ExecutorService executor = Executors.newFixedThreadPool(50);
	        CountDownLatch latch = new CountDownLatch(254);
	        for (int i = 1; i <= 254; i++) {
	            String host = subnet + i;
	            executor.submit(() -> {
	                try {
	                    InetAddress.getByName(host).isReachable(200);
	                } catch (Exception ignored) {}
	                latch.countDown();
	            });
	        }
	        latch.await();
	        executor.shutdown();

	        Process p = Runtime.getRuntime().exec("arp -a");
	        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
	        String linha;

	        while ((linha = reader.readLine()) != null) {
	            linha = linha.trim();

	            // Tenta capturar IP e MAC (Windows)
	            if (linha.matches(".*\\d+\\.\\d+\\.\\d+\\.\\d+.*")) {
	                String[] partes = linha.split("\\s+");

	                if (partes.length >= 2) {
	                    String ip = partes[0];
	                    String mac = partes[1];

	                    if ((ip.startsWith("192.") || ip.startsWith("10.") || ip.startsWith("172.")) &&
	                            !mac.equalsIgnoreCase("ff-ff-ff-ff-ff-ff")) {

	                        dispositivos.add(new Dispositivo("Dispositivo", ip, mac));
	                    }
	                }
	            }
	        }

	        p.waitFor();

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return dispositivos;
	}

	public static String identificar(String ip, String mac) {
	    try {
	        String ultimoOctetoStr = ip.substring(ip.lastIndexOf('.') + 1);
	        int ultimoOcteto = Integer.parseInt(ultimoOctetoStr);
	        if (ultimoOcteto == 1) {
	            return "Provável Roteador (fim em .1)";
	        }
	    } catch (Exception ignored) {}

	    List<Integer> portasComuns = List.of(
	        21, 22, 23, 53, 80, 110, 123, 135, 137, 138, 139, 143, 161, 389,
	        443, 445, 554, 587, 631, 993, 995, 3306, 3389, 5432, 5900,
	        8000, 8080, 8443, 9000, 9090, 49152, 5555, 5353, 62078, 1900
	    );

	    ExecutorService executor = Executors.newFixedThreadPool(5);
	    List<Future<Integer>> resultados = new ArrayList<>();

	    for (int porta : portasComuns) {
	        resultados.add(executor.submit(() -> {
	            try (Socket socket = new Socket()) {
	                socket.connect(new InetSocketAddress(ip, porta), 200);
	                return porta;
	            } catch (Exception e) {
	                return -1;
	            }
	        }));
	    }

	    executor.shutdown();

	    List<Integer> portasAbertas = new ArrayList<>();
	    for (Future<Integer> futuro : resultados) {
	        try {
	            int porta = futuro.get();
	            if (porta != -1) portasAbertas.add(porta);
	        } catch (Exception ignored) {}
	    }

	    // Heurísticas baseadas nas portas abertas
	    if (portasAbertas.contains(554)) {
	        return "Possível Câmera IP (RTSP na porta 554)";
	    }
	    if (portasAbertas.contains(3389)) {
	        return "PC com Windows (Acesso remoto via RDP)";
	    }
	    if (portasAbertas.contains(445) || portasAbertas.contains(139)) {
	        return "PC com Windows (Compartilhamento de arquivos SMB)";
	    }
	    if (portasAbertas.contains(22) && portasAbertas.contains(80)) {
	        return "Servidor Linux com Web e SSH";
	    }
	    if (portasAbertas.contains(22)) {
	        return "Servidor Linux ou Raspberry Pi (SSH aberto)";
	    }
	    if (portasAbertas.contains(80) && portasAbertas.contains(443)) {
	        return "Servidor Web (HTTP e HTTPS abertos)";
	    }
	    if (portasAbertas.contains(8080) || portasAbertas.contains(8000) || portasAbertas.contains(9000)) {
	        return "Servidor Web alternativo ou DVR";
	    }
	    if (portasAbertas.contains(5555)) {
	        return "Dispositivo Android (ADB na porta 5555)";
	    }
	    if (portasAbertas.contains(62078)) {
	        return "Dispositivo Apple (iPhone via iTunes Lockdown)";
	    }
	    if (portasAbertas.contains(5353)) {
	        return "Dispositivo Apple ou Android (mDNS ativo)";
	    }
	    if (portasAbertas.contains(3306)) {
	        return "Servidor com MySQL (porta 3306)";
	    }
	    if (portasAbertas.contains(5432)) {
	        return "Servidor com PostgreSQL (porta 5432)";
	    }
	    if (portasAbertas.contains(5900)) {
	        return "Servidor com VNC remoto (porta 5900)";
	    }

	    // Verificação por prefixo MAC
	    if (mac != null && !mac.isEmpty()) {
	        try {
	            String prefixo = mac.toUpperCase().replace(":", "").substring(0, 6);
	            switch (prefixo) {
	                case "F45C89": case "A434F1":
	                    return "Celular LG ou Samsung (MAC)";
	                case "001A11": case "B827EB": case "3C5A37":
	                    return "Dispositivo Apple ou Raspberry Pi (MAC)";
	                case "FCC2DE": case "0024D7":
	                    return "Notebook ou PC HP (MAC)";
	                case "FCFBFB":
	                    return "Dispositivo Xiaomi (MAC)";
	            }
	        } catch (Exception ignored) {}
	    }

	    if (!portasAbertas.isEmpty()) {
	        return "Dispositivo com portas abertas: " + portasAbertas;
	    } else {
	        return "Dispositivo desconhecido ou com todas as portas fechadas";
	    }
	}
	
}
