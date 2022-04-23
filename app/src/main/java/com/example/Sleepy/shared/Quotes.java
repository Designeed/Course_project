package com.example.Sleepy.shared;

import java.util.Random;

public class Quotes {
    private static final String[] quoteArrayAlarm = new String[]{
            "Здесь мог бы быть анекдот про Штирлица",
            "За дверью послышались споры\n-Грибы, - подумал Штирлиц",
            "Труднее всего вылезать из долгов и из постели в холодное утро",
            "Утро слишком прекрасно, чтобы длиться долго",
            "Счастье — когда утром хочется на работу, а вечером — домой",
            "Каждое утро — это время начать жизнь снова",
            "Считают, что успех приходит к тем, кто рано встает. Нет: успех приходит к тем, кто встает в хорошем настроении",
            "Лишь тот, кто познал ужас ночи, может понять сладость наступления утра",
            "Утро добрым остаётся, пока шанс ему даётся",
            "Мало встать рано утром, надо ещё перестать спать",
            "Нельзя просто так взять и встать рано утром. Это всегда сложный философский процесс",
            "Каждое утро — это шанс начать жизнь заново",
            "Немыслимое счастье проснуться утром, имея впереди целый день, огромный день, полный радостей и забот",
            "Я имею счастье проводить время с утра и до вечера в обществе гениального человека, то есть с самим собой, а это очень приятно",
            "У вас должна быть мечта, чтобы вы могли вставать по утрам",
            "Я стал взрослым. Могу делать всё, что захочу. А просто хочется спать…",
            "Если волк молчит, лучше его не перебивать",
            "У батарее есть один минус\nи один плюс",
            "Легко вставать, когда ты не ложился",
            "Лучше светлое пиво, чем темное будущее",
            "Я просто будильник",
            "Прости, если разбудил",
            "Реклама в будильнике?\nRaid: Shadow Legends RPG Strategy Game...",
            "Raid: Shadow Legends RPG Strategy Game",
            "10 раз хлопни, посмотри на потолок, 3 раза моргни, расскажи это 5 своим друзьям, посмотри под подушку и там IPad Air"
    };

    private static final String[] quoteArrayCat = new String[]{
            "Привет!",
            "Hello!",
            "Buon!",
            "Buenos!",
            "Ola!",
            "Мяу",
            "meow",
            "(づ｡◕‿‿◕｡)づ",
            "(｡◕‿‿◕｡)",
            "(≧◡≦)",
            "(ღ˘⌣˘ღ)",
            "(⁄ ⁄•⁄ω⁄•⁄ ⁄)",
            "(=^･ｪ･^=)",
            "ʕ ᵔᴥᵔ ʔ",
            "Дай поспать!",
            "Спокойной ночи!",
            "Иди поспи",
            "Нажми на меня еще раз",
            "Хочешь анекдот?"
    };

    private static final String[] anecdotesArray = new String[]{
            "Штирлиц залез на телеграфный столб и, чтобы не привлекать внимания прохожих, развернул газету.",
            "За дверью послышались споры\n-Грибы, - подумал Штирлиц",
            "Приходит слепой в магазин берет собаку-поводыря и начинает раскручивать ее над головой\n" +
                    "- Что вы делаете?!\n" +
                    "- Да так, осматриваюсь",
            "Два психа сидят в коридоре психушки. Мимо проходит медсестра и пытается открыть какую-то дверь. " +
                    "У нее не получается. Психи: «Дергай сильнее». Она дергает ручку сильнее, дверь все равно не открывается. " +
                    "Медсестра, задумчиво: «Наверное, закрыто». Психи: «Да, закрыто… А мы думали тебе ручка нужна».",
            "Эстонские криминалисты установили, что в Кеннеди стреляли не из окна, а из винтовки",
            "Купил мужчина шляпу, а она ему как раз.",
            "Заходят как-то украинский кот со своим хозяином к ветеринару. " +
                    "Ветеринар смотрит на кота и говорит: «Привит?», а кот отвечает: «Здравствуйте».",
            "Трое неизвестных забрали у прохожего паспорт и порвали. \n\n\n\nТеперь неизвестных четверо.",
            "Бабка, заметившая в маршрутке свободное сидение, выпрыгнула из такси.",
            "Чеченец забыл, как по-русски будет \"Два\". Заходит в магазин и говорит: \"Три батона, а один не надо\".",
            "Чем отличается десантник от сапера? — направлением полета.",
            "Скачет Илья Муромец по пустыне, устал, силы на исходе, видит вдали оазис вода и еда, и там же Змей Горыныч." +
                    " Илья Муромец достал свой меч и в бой с Змей Горынычем, бьётся день и ночь с ним в жестоком бою, " +
                    "на третий день Змей Горыныч спрашивает у Богатыря, да что ж тебе надо от меня?\n" +
                    "- Да пить я хочу.\n" +
                    "- Да пей, что докопался то?!"
    };

    private static final String[] quoteArraySloth = new String[]{
            "Привет!",
            "Hello!",
            "Buon!",
            "Buenos!",
            "Ola!",
            "(づ｡◕‿‿◕｡)づ",
            "(｡◕‿‿◕｡)",
            "(≧◡≦)",
            "(ღ˘⌣˘ღ)",
            "(⁄ ⁄•⁄ω⁄•⁄ ⁄)",
            "(=^･ｪ･^=)",
            "ʕ ᵔᴥᵔ ʔ",
            "Спокойной ночи!",
            "Иди поспи",
            "Нажми на меня еще раз",
            "Хочешь анекдот?",
            "Ты очень любопытный"
    };

    private static final String[] factsArray = new String[]{
            "Каждое лето Эйфелева башня становится на 15 сантиметров выше.",
            "Зубы бобров никогда не перестают расти.",
            "В теле среднестатистического человека достаточно железа, чтобы сделать гвоздь длиной 7,62 сантиметра.",
            "141 078 лет – самый длинный тюремный срок за всю историю!",
            "Морской конёк – самая медленная рыба на Земле!",
            "В Вирджинии (штат США) курицам не разрешают откладывать яйца до 8:00 утра, и они должны успеть сделать это до 4:00 вечера!",
            "Чтобы зарядить один iPhone, нужно 595 апельсинов.",
            "Есть несколько видов ленточных червей, которые съедают себя сами, если не найдут никакой пищи.",
            "Коалы спят 22 часа в сутки!",
            "Автомобиль «жук» был идеей, придуманной Адольфом Гитлером.",
            "Самое длинное название книги за всю историю состоит из 670 слов.",
            "Улитки могут спать три года подряд.",
            "Тома и Джерри первоначально звали Джаспером и Джинксом.",
            "Язык хамелеона длиннее, чем его тело.",
            "Анатидеофобия – это навязчивый страх, что из другого уголка мира за вами наблюдает утка."
    };

    public static String getQuoteAlarm(){
        return quoteArrayAlarm[new Random().nextInt(quoteArrayAlarm.length)];
    }

    public static String getQuoteCat(){
        return quoteArrayCat[new Random().nextInt(quoteArrayCat.length)];
    }

    public static String getAnecdote() {
        return anecdotesArray[new Random().nextInt(anecdotesArray.length)];
    }

    public static String getQuoteSloth(){
        return quoteArraySloth[new Random().nextInt(quoteArraySloth.length)];
    }

    public static String getFact(){
        return factsArray[new Random().nextInt(factsArray.length)];
    }
}
