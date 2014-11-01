package com.hackathon.hackathon2014.webservice.task;

import android.os.AsyncTask;
import android.util.Log;

import com.hackathon.hackathon2014.model.Category;
import com.hackathon.hackathon2014.model.Question;
import com.hackathon.hackathon2014.utility.PostRequestHandler;

import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Krai on 11/1/14 AD.
 */
public class QuestionRequestTask extends AsyncTask<Void,Void,List<Question>> {

    private final String url = "http://api.radar.codedeck.com/questions";
    private RestTemplate restTemplate;
    private PostRequestHandler<List<Question>> handler;

    public QuestionRequestTask(RestTemplate restTemplate ,PostRequestHandler<List<Question>> handler) {
        this.restTemplate = restTemplate;
        this.handler = handler;
    }

    @Override
    protected List<Question> doInBackground(Void... voids) {
        List<Question> questions = new ArrayList<Question>();

        try {
            questions.addAll(Arrays.asList(restTemplate.getForObject(url, Question[].class)));
        } catch (Exception e) {
            String error = "Fail !!!!!!" + e.getMessage();
            Log.e(this.getClass().getName(), error);
        }

        //MOCK
        questions.add(
                new Question("อาหาร" + "ที่คุณชอบ"
                        ,new Category("ญี่ปุ่น","เทปันยากิ","ซูชิ")
                        ,new Category("จีน","พระกระโดดกำแพง","หูฉลามน้ำแดง","ติ่มซำ","กระเพาะปลาผัดแห้ง","กระเพาะปลาน้ำแดง","ขาหมูหมั่นโถว")
                        ,new Category("ไทย","กระเพราไข่ดาว", "แกงป้า")
                        ,new Category("อินเดีย")
                        ,new Category("เกาหลี")
                        ,new Category("อิตาเลี่ยน" ,"พิซซ่า","สปาเกตตี้")
                )
        );
        questions.add(new Question("สี" + "ที่คุณชอบ"));
        questions.add(new Question("สัตว์" + "ที่คุณชอบ"));
        questions.add(new Question("Mobile" + "ที่คุณชอบ"));
        questions.add(new Question("เพศ" + "ที่คุณชอบ"
                        ,new Category("ชาย")
                        ,new Category("หญิง")
                )
        );
        questions.add(new Question("ประเภทหนัง" + "ที่คุณชอบ"
                ,new Category("action","เลือดสาด")
                ,new Category("ตลก")
                ,new Category("อินดี้")
        ));
        questions.add(new Question("กีฬา" + "ที่คุณชอบ"));
        questions.add(new Question("เพลง" + "ที่คุณชอบ"));
        questions.add(new Question("เครื่องดื่ม" + "ที่คุณชอบ"));
        questions.add(new Question("Notebook" + "ที่คุณชอบ"));
        questions.add(new Question("ขนม" + "ที่คุณชอบ"));
        questions.add(new Question("ศิลปิน" + "ที่คุณชอบ"));
        questions.add(new Question("หนังสือ" + "ที่คุณชอบ"));
        questions.add(new Question("วิชาเรียน" + "ที่คุณชอบ"));
        questions.add(new Question("Brand เสื้อผ้า" + "ที่คุณชอบ"));
        questions.add(new Question("งานอดิเรก"));
        questions.add(new Question("สถานที่เที่ยว" + "ที่คุณชอบ"));

        Log.e(this.getClass().getName(), questions.toString());

        return questions;
    }

    @Override
    protected void onPostExecute(List<Question> questions) {
        handler.handle(questions);
    }
}
