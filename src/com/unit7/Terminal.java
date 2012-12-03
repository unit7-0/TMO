package com.unit7;

/**
 * Created with IntelliJ IDEA.
 * File: Terminal
 * User: unit7
 * Date: 02.12.12
 * Time: 18:44
 */
public class Terminal {
    public Terminal(int channels) {
        this.channels = new Subscriber[channels];
    }

    /**
     * попытка подключить абонента
     */
    public boolean push(Subscriber subscriber) {
        if(busy == channels.length || isInQueue(subscriber))
            return false;

        for(int i = 0; i < channels.length; ++i) {
            if(channels[i] == null) {
                busy += 1;
                channels[i] = subscriber;

                return true;
            }
        }

        return false;
    }

    /**
     * проверка на присутствие пользователя в системе
     */
    public boolean isInQueue(Subscriber subscriber) {
        for(int i = 0; i < channels.length; ++i)
            if(channels[i] != null && channels[i].equals(subscriber))
                return true;

        return false;
    }

    /**
     * отключение абонента
     */
    public boolean pop(Subscriber subscriber) {
        for(int i = 0; i < channels.length; ++i) {
            if(channels[i] != null && channels[i].equals(subscriber)) {
                busy -= 1;
                channels[i] = null;

                return true;
            }
        }

        return false;
    }

    public boolean isFreeChannel() {
        return busy != channels.length;
    }

    int busy;
    Subscriber[] channels;
}
