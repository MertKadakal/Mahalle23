package mert.kadakal.myapplication;

public class Comment {
    private String userName;
    private String commentText;

    public Comment(String userName, String commentText) {
        this.userName = userName;
        this.commentText = commentText;
    }

    public String getUserName() {
        return userName;
    }

    public String getCommentText() {
        return commentText;
    }

    @Override
    public String toString() {
        return userName + ": " + commentText;
    }
}
