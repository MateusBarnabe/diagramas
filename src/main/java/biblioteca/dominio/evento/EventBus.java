package biblioteca.dominio.evento;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Barramento de eventos genérico Stage 3.
 * Permite publicação e assinatura de eventos sem acoplamento.
 * Implementado com generics para suportar qualquer tipo de evento.
 */
public class EventBus<T> {
    private final List<Consumer<T>> assinantes = new ArrayList<>();

    /**
     * Registra um assinante para os eventos deste barramento.
     * 
     * @param handler função a ser executada quando um evento é publicado
     */
    public void assinar(Consumer<T> handler) {
        assinantes.add(handler);
    }

    /**
     * Publica um evento para todos os assinantes.
     * 
     * @param evento o evento a ser publicado
     */
    public void publicar(T evento) {
        assinantes.forEach(h -> h.accept(evento));
    }

    /**
     * Retorna a quantidade de assinantes.
     */
    public int quantidadeAssinantes() {
        return assinantes.size();
    }
}
