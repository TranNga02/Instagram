package com.example.instagram.viewmodel;

import com.google.firebase.Timestamp;

import java.util.Date;

public class CaculateTime {
    private static CaculateTime instance;

    public static CaculateTime getInstance() {
        if (instance == null) {
            instance = new CaculateTime();
        }
        return instance;
    }

    public String caculateTime(Timestamp time){
        // Lấy ngày hiện tại
        Date currentDate = new Date();

        // Chuyển đổi Timestamp thành Date
        Date timestampDate = time.toDate();

        // Tính số milliseconds giữa ngày hiện tại và ngày của Timestamp
        long timeDifferenceInMillis = Math.abs(currentDate.getTime() - timestampDate.getTime());

        // Chuyển đổi số milliseconds thành số ngày
        int numberOfDay = (int) (timeDifferenceInMillis / (24 * 60 * 60 * 1000));

        if(numberOfDay > 0 ) return numberOfDay + " ngay truoc";
        else{
            int numberOfHours = (int) (timeDifferenceInMillis / (60 * 60 * 1000));
            if(numberOfHours > 0) return numberOfHours + " gio truoc";
            else if(timeDifferenceInMillis >= 60 *1000) {
                return ((int) (timeDifferenceInMillis / (60 * 1000))) + " phut truoc";
            }
            else return ((int) (timeDifferenceInMillis / 1000)) + " giay truoc";
        }
    }
}
