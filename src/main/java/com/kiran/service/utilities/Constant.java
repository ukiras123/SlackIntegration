package com.kiran.service.utilities;

import java.util.Random;

/**
 * Created by Kiran on 12/18/17.
 */
public class Constant {

    public static String getACompliment() {
        String[] compliments = {"Having you on the team makes a huge difference. ",
                "You always find a way to get it done — and done well!",
                "It’s really admirable how you always see projects through from conception to completion.",
                "Thank you for always speaking up in team meetings and providing a unique perspective.",
                "Your efforts at strengthening our culture are not unnoticed.",
                "Fantastic work!",
                "Ты умный!",
                "Even when the going gets tough, you continue to have the best attitude!",
                "It’s amazing how you always help new the team get up to speed.",
                "Wow! Just when I thought your work couldn’t get any better!",
                "I couldn’t imagine working without you!",
                "Your work ethic speaks for itself.",
                "Я знаю, что это был трудный проект, но ваши результаты превзошли мои ожидания.",
                "There’s no other way to say it: we’d be lost without you.",
                "Thanks for always being willing to lend a hand.",
                "The pride you take in your work is truly inspiring.",
                "You’re so great to work with.",
                "I am continually impressed by the results you produce!",
                "Thank you for being so flexible.",
                "আপনার কাজ নীতিবিজ্ঞান এই বিশ্বের বাইরে",
                "It’s incredible how thorough your work is.",
                "Your work ethic is out of this world!",
                "You have an extremely healthy perspective.",
                "You’re really good at cheering everybody up!",
                "Is there anything you can’t do?!",
                "Napakagaling mong magtrabaho kasama",
                "You are one of the most reliable team mate I’ve ever had.",
                "Thank you for setting a great example for your coworkers.",
                "Not everyone is as creative as you — I mean it!",
                "It’s amazing how you’re always able to overcome any obstacle thrown your way. ",
                "Keep up the great work!",
                "I was blown away by your contributions this week.",
                "I really enjoy working with you.",
                "मैं वास्तव में आपके साथ काम करने का आनंद लेता हूं",
                "You’re awesome!",
                "Amazing job on that project — I really mean it.",
                "You are an invaluable member of the team.",
                "আপনি একটি ভয়ঙ্কর দল সদস্য",
                "To be honest, I’m jealous of your talents.",
                "I can’t believe how lucky I am to have a great co-worker like you.",
                "You come up with fantastic ideas!",
                "What’s your secret? Your output is impressive, to say the least.",
                "You're like sunshine on a rainy day.",
                "Your smile is contagious.",
                "You look great today.",
                "Nagmumula ka sa mga kamangha-manghang ideya",
                "Thanks for always being willing to lend a hand.",
                "Wow! Nice work. ",
                "वाह! अच्छा काम।",
                "You are making a difference.",
                "Your work ethic is enviable.",
                "তুমিই শ্রেষ্ঠ",
                "Treat yourself to another compliment!",
                "salamat sa iyong hirap sa trabaho",
                "It’s incredible how often you go above and beyond.",
                "I just wanted to let you know how much you mean to the team.",
                "How did this place ever operate without you?!",
                "I know I’ve been busy lately, but I just had to tell you what a great team member you are.",
                "You play a crucial role in our company’s success.",
                "It’s so obvious how you pay attention to detail.",
                "You are always so quick to show initiative.",
                "It’s honestly hard to explain how integral you are to this team.",
                "You are an awesome team member!",
                "If you were running for president, I would vote for you.",
                "It’s incredible how often you go above and beyond.",
                "Your work never ceases to amaze me!",
                "I couldn’t live with myself if I didn’t tell you how much you mean to this company.",
                "Things have definitely been crazy lately, but you’re crushing it!",
                "You’re always on time to things, which is awesome because I’m not."};

        Random random = new Random();
        int index = random.nextInt(compliments.length);
        return compliments[index];
    }

    public static String getATeamCompliment() {
        String[] compliments = {"Having you all on the team makes a huge difference. ",
                "You guys always find a way to get it done — and done well!",
                "This team's efforts at strengthening our culture are not unnoticed.",
                "Fantastic work guys!",
                "Even when the going gets tough, this team continues to have the best attitude!",
                "I couldn’t imagine working without you guys!",
                "There’s no other way to say it: we’d be lost without your team effort.",
                "This team is so great to work with.",
                "I am continually impressed by the results this team produces!",
                "This team's work ethic is out of this world!",
                "Is there anything this team can’t do?!",
                "Keep up the great work guys, we will have a team happy hour soon!",
                "What a great accomplishment.",
                "You guys are awesome!",
                "It’s incredible how often this team goes above and beyond.",
                "I just wanted to let you know how much you all mean to the team.",
                "This team plays a crucial role in our company’s success."};

        Random random = new Random();
        int index = random.nextInt(compliments.length);
        return compliments[index];
    }

}
