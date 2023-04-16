public class Question {
    private String question;
    private String answer;
    private String fake1;
    private String fake2;
    private String fake3;

    public String getQuestion() {
        return this.question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return this.answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getFake1() {
        return this.fake1;
    }

    public void setFake1(String fake1) {
        this.fake1 = fake1;
    }

    public String getFake2() {
        return this.fake2;
    }

    public void setFake2(String fake2) {
        this.fake2 = fake2;
    }

    public String getFake3() {
        return this.fake3;
    }

    public void setFake3(String fake3) {
        this.fake3 = fake3;
    }



    public Question(String q, String a, String f1, String f2, String f3){
        this.question = q;
        this.answer = a;
        this.fake1 = f1;
        this.fake2 = f2;
        this.fake3 = f3;
    }

    public Question(String in){
        String[] arr = in.split("~");
        this.question = arr[0];
        this.answer = arr[1];
        this.fake1 = arr[2];
        this.fake2 = arr[3];
        this.fake3 = arr[4];
    }

    public String stringify(){
        return question+"~"+
                answer+"~"+
                fake1+"~"+
                fake2+"~"+
                fake3;
    }

    public static Question fromString(String  in){
        String[] arr = in.split("~");
        return new Question(arr[0], arr[1], arr[2], arr[3], arr[4]);
    }



}
