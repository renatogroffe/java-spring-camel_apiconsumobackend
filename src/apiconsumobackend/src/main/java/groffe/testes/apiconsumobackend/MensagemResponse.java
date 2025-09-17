package groffe.testes.apiconsumobackend;

public class MensagemResponse {
    private String id;
    private String mensagem;
    private String retornoBackEnd;

    public MensagemResponse() {}

    public MensagemResponse(String id, String mensagem, String retornoBackEnd) {
        this.id = id;
        this.mensagem = mensagem;
        this.retornoBackEnd = retornoBackEnd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getRetornoBackEnd() {
        return retornoBackEnd;
    }

    public void setRetornoBackEnd(String retornoBackEnd) {
        this.retornoBackEnd = retornoBackEnd;
    }
}