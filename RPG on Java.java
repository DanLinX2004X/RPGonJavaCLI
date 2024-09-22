import java.util.Scanner;
import java.util.Random;

// Общий базовый класс для персонажей и врагов
abstract class Character {
    protected String name;
    protected int health;
    protected int mana;
    protected int strength;
    protected int agility;
    protected int intelligence;
    protected int attackDamage;
    protected int experience;
    protected int level;
    protected int stamina;

    public Character(String name, int health, int mana, int strength, int agility, int intelligence, int attackDamage, int stamina) {
        this.name = name;
        this.health = health;
        this.mana = mana;
        this.strength = strength;
        this.agility = agility;
        this.intelligence = intelligence;
        this.attackDamage = attackDamage;
        this.stamina = stamina;
        this.experience = 0;
        this.level = 1;
    }

    public abstract void attack(Character target);

    public void defend() {
        if (stamina >= 20) {
            System.out.println(name + " защищается.");
            stamina -= 20;
        } else {
            System.out.println(name + " слишком устал, чтобы защищаться!");
        }
    }

    public boolean isAlive() {
        return health > 0;
    }

    public void gainExperience(int exp) {
        this.experience += exp;
        if (this.experience >= 100) {
            this.level++;
            this.experience = this.experience % 100;
            System.out.println(name + " достиг(ла) уровня " + level + "!");
        }
    }

    public void receiveDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) this.health = 0;
        System.out.println(name + " получает " + damage + " урона. Здоровье: " + this.health);
    }

    public void interact(Character target, Random random, String battlefield) {
        int outcome = random.nextInt(100);
        switch (battlefield) {
            case "Белая комната":
                if (outcome < 25) {
                    System.out.println(name + "преисполнился в своём познании и увернулся урона.");
                    stamina += 10;
                } else if (outcome < 50) {
                    System.out.println(target.name + " преисполнился в своём познании и зарегенился ЖОстко.");
                    target.health += 20;
                    if (target.health > 100) target.health = 100;
                } else if (outcome < 75) {
                    System.out.println(name + " преисполнился в своём познании. Урон повышен.");
                    this.attackDamage += 10;
                } else {
                    System.out.println(name + " не переварил больше трёх букв, и " + target.name + " подкрался и нанёс удар.");
                    this.receiveDamage(10);
                }
                break;
            case "Тюрьма Дружбы":
                if (outcome < 25) {
                    System.out.println(name + " спрятался и вертел этот урон.");
                    stamina += 10;
                } else if (outcome < 50) {
                    System.out.println(name + " нашёл сломанный туалет и усилил свою атаку.");
                    this.attackDamage += 15;
                } else if (outcome < 75) {
                    System.out.println(name + " Нашёл и кинул нож-бабочка,  " + target.name + " получает 10 урона.");
                    target.receiveDamage(10);
                } else {
                    System.out.println(target.name + " Увидел какой-то постер. Поднял мотивацию и ЖОстко зарегенился.");
                    target.mana += 20;
                    if (target.mana > 100) target.mana = 100;
                }
                break;
            case "Город":
                if (outcome < 25) {
                    System.out.println(name + " спрятался в баре и не получает урона.");
                    stamina += 10;
                } else if (outcome < 50) {
                    System.out.println(target.name + " нашёл Святой источник и восстановил здоровья.");
                    target.health += 20;
                    if (target.health > 100) target.health = 100;
                } else if (outcome < 75) {
                    System.out.println(name + " Выпел РедБулл. Атака увеличена на 2 хода.");
                    this.attackDamage += 10;
                } else {
                    System.out.println(target.name + " сбежал в переулке и уклоняется от следующей атаки.");
                }
                break;
        }
    }

    public void restoreStamina() {
        this.stamina += 10;
        if (this.stamina > 250) this.stamina = 250;
    }

    public boolean dodge(Random random) {
        return random.nextInt(100) < 20; // 20% шанс уворота
    }

    protected void stun(int turns) {
    }

    public void castSpell(Character target, String spell, Random random) {
    }
}

// Воин
class Warrior extends Character {
    public Warrior(String name) {
        super(name, 300, 30, 10, 5, 2, 15, 250);
    }

    @Override
    public void attack(Character target) {
        if (stamina >= 15) {
            if (!target.dodge(new Random())) {
                System.out.println(name + " херачит мечом!");
                target.receiveDamage(this.attackDamage);
            } else {
                System.out.println(target.name + " увернулся от атаки!");
            }
            stamina -= 15;
        } else {
            System.out.println(name + " устал крутит этот меч!");
        }
    }
}

// Чародей
class Mage extends Character {
    public Mage(String name) {
        super(name, 200, 130, 3, 4, 120, 20, 300);
    }

    @Override
    public void attack(Character target) {
        if (stamina >= 15) {
            if (mana >= 10) {
                if (!target.dodge(new Random())) {
                    System.out.println(name + " херачит заклинание!");
                    target.receiveDamage(this.attackDamage);
                } else {
                    System.out.println(target.name + " на пальце вертел заклинание!");
                }
                mana -= 10;
            } else {
                System.out.println(name + " недостаточно маны для атаки!");
            }
            stamina -= 15;
        } else {
            System.out.println(name + " хочет отдохнуть!");
        }
    }

    @Override
    public void castSpell(Character target, String spell, Random random) {
        switch (spell) {
            case "Банкай":
                if (mana >= 55) {
                    System.out.println(name + " использует Банкай Тенса Зангецу'!");
                    if (random.nextInt(100) < 67) {
                        target.receiveDamage(35);
                    } else {
                        System.out.println(target.name + " Уцелел от Банкая '!");
                    }
                    mana -= 55;
                } else {
                    System.out.println(name + " Недостаточно сил для банкая'!");
                }
                break;
            case "РаСшИрЕнИе ТеРрИтОрИи":
                if (mana >= 30) {
                    System.out.println(name + " активировал РаСшИрЕнИе ТеРрИтОрИи мЮрЮ КюСу'!");
                    if (random.nextInt(100) < 35) {
                        System.out.println(target.name + " ошарашен защита снижена на 5 единиц хы");
                        // Пример механики уменьшения защиты врага
                    } else {
                        System.out.println(target.name + " крутой, выжил в РаСшИрЕнИе ТеРрИтОрИи !");
                    }
                    mana -= 30;
                } else {
                    System.out.println(name + " нет сил для этого говно. Ну и правильно, банкай лучше!");
                }
                break;
            case "ПукПук":
                if (mana >= 150) {
                    System.out.println(name + " кастует 'ПукПук'!");
                    if (random.nextInt(100) < 95) {
                        System.out.println(target.name + " в шоке от этого, теперь не могёт в два хода");
                        // Пример механики, где враг не может уклоняться
                    } else {
                        System.out.println(target.name + " пофиг на твой пердёж");
                    }
                    mana -= 150;
                } else {
                    System.out.println(name + " нет газаов в организме");
                }
                break;
        }
    }
}

// Лучник
class Archer extends Character {
    public Archer(String name) {
        super(name, 150, 50, 7, 10, 4, 12, 250);
    }

    @Override
    public void attack(Character target) {
        if (stamina >= 15) {
            if (!target.dodge(new Random())) {
                System.out.println(name + " шмаляет из лука!");
                target.receiveDamage(this.attackDamage);
            } else {
                System.out.println(target.name + " вертел твои стрелы");
            }
            stamina -= 15;
        } else {
            System.out.println(name + " заколебался");
        }
    }
}

// Враг
class Enemy extends Character {
    public Enemy(String name, int health, int attackDamage) {
        super(name, health, 50, 8, 6, 5, attackDamage, 250);
    }

    @Override
    public void attack(Character target) {
        if (!target.dodge(new Random())) {
            System.out.println(name + " атакует " + target.name + "!");
            target.receiveDamage(this.attackDamage);
        } else {
            System.out.println(target.name + " увернулся от атаки!");
        }
    }

    @Override
    public void castSpell(Character target, String spell, Random random) {
        // Добавить заклинания для врагов
        switch (spell) {
            case "Цукуёми":
                if (mana >= 40) {
                    System.out.println(name + " использует 'Цукуёми'!");
                    target.receiveDamage(30);
                    mana -= 40;
                } else {
                    System.out.println(name + " недостаточно маны для 'ТЦукуёми'!");
                }
                break;
            // Добавить другие заклинания для врагов по необходимости
        }
    }
}

// Основной игровой класс
class TextAdventureGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        // Выбор класса персонажа
        System.out.println("Выберите класс персонажа: 1 - Воин, 2 - Чародей, 3 - Лучник.");
        int characterClass = scanner.nextInt();
        scanner.nextLine(); // Чтение оставшегося переноса строки

        System.out.print("Введите имя персонажа: ");
        String name = scanner.nextLine();

        Character player;
        switch (characterClass) {
            case 1:
                player = new Warrior(name);
                break;
            case 2:
                player = new Mage(name);
                break;
            case 3:
                player = new Archer(name);
                break;
            default:
                System.out.println("Некорректный выбор. Создаётся Воин, по дефолту.");
                player = new Warrior(name);
                break;
        }

        // Создаем врага
        Enemy enemy;
        String[] enemyNames = {"Юичи Катагири", "Аянокоджи Киётака", "Баку Мадармэ", "Йохан Либерт"};
        int[] enemyHealth = {80, 60, 100, 120};
        int[] enemyAttackDamage = {10, 15, 5, 8};
        int enemyChoice = random.nextInt(enemyNames.length);
        enemy = new Enemy(enemyNames[enemyChoice], enemyHealth[enemyChoice], enemyAttackDamage[enemyChoice]);

        System.out.println("На вас нападает " + enemy.name + "!");

        System.out.println("Выберите поле боя: 1 - Белая комната, 2 - Тюрьма Дружбы, 3 - Город");
        int battlefieldChoice = scanner.nextInt();
        scanner.nextLine(); // Чтение оставшегося переноса строки

        String battlefield = "";
        switch (battlefieldChoice) {
            case 1:
                battlefield = "Белая комната";
                break;
            case 2:
                battlefield = "Тюрьма Дружбы";
                break;
            case 3:
                battlefield = "Город";
                break;
            default:
                System.out.println("Похоже вы неуклюжий тип, пожалуй выберем для вас Б Е Л У Ю___ К О М Н А Т У.");
                battlefield = "Белая комната";
                break;
        }

        Random battlefieldRandom = new Random();
        int playerTurnsToRest = 0; // Количество ходов, которые персонаж должен пропустить из-за усталости
        int attackBoostTurns = 0; // Количество ходов, в течение которых урон игрока увеличен

        // Основной игровой цикл
        while (player.isAlive() && enemy.isAlive()) {
            if (playerTurnsToRest > 0) {
                System.out.println(player.name + " Эх, устал чеееел.");
                playerTurnsToRest--;
                player.restoreStamina(); // Восстановление стамины во время отдыха
            } else {
                if (attackBoostTurns > 0) {
                    attackBoostTurns--;
                } else {
                    // Сбросить усиление атаки после 2 ходов
                    if (player.attackDamage > 10) {
                        player.attackDamage -= 10; // Пример возвращения к базовому урону
                    }
                }

                System.out.println("Выберите действие: 1 - Атака, 2 - Защита, 3 - Взаимодействовать");
                if (player instanceof Mage) {
                    System.out.println("4 - Странные Способности");
                }
                int action = scanner.nextInt();

                switch (action) {
                    case 1:
                        player.attack(enemy);
                        break;
                    case 2:
                        player.defend();
                        break;
                    case 3:
                        player.interact(enemy, random, battlefield);
                        if (player.attackDamage > 10) { // Проверка на усиление атаки
                            attackBoostTurns = 2; // Увеличение урона на 2 хода
                        }
                        break;
                    case 4:
                        if (player instanceof Mage) {
                            System.out.println("Что выхотите замутить: 1 - 'Банкай', 2 - 'РаСшИрЕнИе ТеРрИтОрИи', 3 - 'ПукПук'");
                            int spellChoice = scanner.nextInt();
                            switch (spellChoice) {
                                case 1:
                                    player.castSpell(enemy, "Банкай", random);
                                    break;
                                case 2:
                                    player.castSpell(enemy, "РаСшИрЕнИе ТеРрИтОрИи", random);
                                    break;
                                case 3:
                                    player.castSpell(enemy, "ПукПук", random);
                                    break;
                            }
                        }
                        break;
                    default:
                        System.out.println("Вам чистые два бала и пропуск хода (Уно карточка).");
                        break;
                }

                // Ход врага, если он жив
                if (enemy.isAlive()) {
                    if (random.nextInt(100) < 25 && (enemy.name.equals("Юичи Катагири") || enemy.name.equals("Аянокоджи Киётака"))) {
                        enemy.castSpell(player, "Цукуёми", random);
                    } else {
                        enemy.attack(player);
                    }
                }
            }

            // Проверка на победу
            if (enemy.isAlive()) {
                System.out.println("Враг жив, продолжайте бой!");
            } else {
                System.out.println(player.name + " уничтожил всех врагов!");
                player.gainExperience(50); // Пример опыта за победу
                return; // Завершить игру
            }
        }

        if (player.isAlive()) {
            System.out.println(player.name + " уничтожил всех в бою!");
        } else {
            System.out.println(player.name + " сдох в бою.");
        }
    }
}
