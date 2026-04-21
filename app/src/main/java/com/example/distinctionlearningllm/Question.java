package com.example.distinctionlearningllm;

//Stores the questions, they have a body, answers, and then an index for the correct answer. Serializable so we can pass them through bundles
public class Question implements java.io.Serializable{
    String body;
    String answerZero, answerOne, answerTwo, answerThree;
    int answerIndex;

    public Question(String body, String answerZero, String answerOne, String answerTwo, String answerThree, int answerIndex)
    {
        this.body = body;
        this.answerZero = answerZero;
        this.answerOne = answerOne;
        this.answerTwo = answerTwo;
        this.answerThree = answerThree;
        this.answerIndex = answerIndex;
    }
}
