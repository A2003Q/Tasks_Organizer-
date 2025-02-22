package Task.example.demo.DAO;

public class MessageDTO {
    private String senderEmail;
    private String receiverEmail;
    private String content;

    // Getters and setters...

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;

    }
}

