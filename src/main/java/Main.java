import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        System.out.println("Задача 2. Долой пробелы (server)");

        int port = 9533;
        //  Занимаем порт, определяя серверный сокет
        ServerSocketChannel serverChannel = null;
        try {
            serverChannel = ServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress("localhost", port));
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            //  Ждем подключения клиента и получаем потоки для дальнейшей работы
            try (SocketChannel socketChannel = serverChannel.accept()) {

                //  Определяем буфер для получения данных
                final ByteBuffer buffer = ByteBuffer.allocate(2 << 10);

                while (socketChannel.isConnected()) {

                    //  читаем данные из канала в буфер
                    int bytesCount = socketChannel.read(buffer);

                    //  если из потока читать нельзя, перестаем работать с этим клиентом
                    if (bytesCount == -1) {
                        System.out.println("и закончили");
                        break;
                    }

                    //  получаем переданную от клиента строку в нужной кодировке и очищаем буфер
                    final String text = new String(buffer.array(), 0, bytesCount, StandardCharsets.UTF_8);

                    buffer.clear();
                    final String result = text.replaceAll(" ", "");
                    System.out.println(result);
                    socketChannel.write(ByteBuffer.wrap((result).getBytes(StandardCharsets.UTF_8)));

                }
            } catch (IOException err) {
                System.out.println(err.getMessage());
            }
        }
    }


}
