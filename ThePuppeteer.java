/*

@author theonelog

imports are the ones neccessary for the program to run

*/
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class ThePuppeteer{
    public static void main(String[] args) {
        System.out.println("                        T H E    P U P P E T E E R");
        System.out.println("");
        Game g1 = new Game();
        g1.run();
    }
}
/*
   The class that manages what goes into an instane of the game. 
   The instance variables are:

   chapters - a linkedlist with all the chapters inside of it. 
   currentChp - an instance var to hold the current chapter to make the code more efficient.   
*/
class Game{
   private final LinkedList<Chapter> chapters = new LinkedList<>();
   private Chapter currentChp;
   /*
      Constructor prints out text stating that a new game is created sucessfully. Also informs the player of the lack of any saves and informs of game length.
      Adds all the chapters to the linked list and sets the currentchp to the tutorial chapter which is chapter 0 internally
   */
   public Game(){
      //Will add the other chapters in the constructor in the future but I only have 1 chapter hard coded so far
      chapters.add(new Tutorial());
      chapters.add(new Chapter1());
   }
   /*
      This method runs the game.
      To begin, it asks the user if they want to go through the tutorial or not
      Depending on the response, it will either go through the tutorial and then start chapter 1 or go straight to chapter 1.
      After completing check1 in chapter 1, check2 will be called in an if statement as this is one of the major branching paths that can occur and if the method returns true, it will go to the branching path.
         Following this branching path, there are multiple times in which a death can occur and one instance that ends the game early due to a player mistake. If one manages to finish the Ex chapter with issue, they will start Ex2
         which is one of the ending chapters in this game.
      If you follow the branching path with check2 returns false, you get put into the Default chapter. Based on your descions in this chapter, you can be put into the Ex track.
         If you decide otherwise, the game will end abrubtly revealing the ending early without any of the additional information that can be gained by going down the Ex storyline.
   */
   public void run(){
      currentChp = chapters.get(0);
      Scanner input = new Scanner(System.in);
      System.out.print("Do you want to go through the tutorial? (Yes or No): ");
      String skip = input.nextLine();
      skip = skip.toLowerCase();
      if ( skip.equals("yes")){
         currentChp.check1();
         currentChp.check2();
      }
      currentChp = chapters.get(1);
      currentChp.check1();
      if (currentChp.check2() == true){
         chapters.add(new Ex());
         currentChp = chapters.get(2);
         currentChp.check1();
          if (currentChp.check2() == true){
               System.out.println("");
               System.out.println("");
               System.out.println("");
               System.out.println("Thank you for playing! Restart the game to approach the game in an entirely new way!");
          }
          else{
            chapters.add(new Ex2());
            currentChp = chapters.get(3);
            currentChp.check1();
            currentChp.check2();
          }     
      }
      else{
         chapters.add(new Default());
         currentChp = chapters.get(2);
         currentChp.check1();
         if (currentChp.check2() == true){
            chapters.add(new Ex());
            currentChp = chapters.get(3);
            currentChp.check1();
            if (currentChp.check2() == true){
               System.out.println("");
               System.out.println("");
               System.out.println("");
               System.out.println("Thank you for playing! Restart the game to approach the game in an entirely new way!");
             }
             else{
               chapters.add(new Ex2());
               currentChp = chapters.get(4);
               currentChp.check1();
               currentChp.check2();

              }
         } 
         else{
            chapters.add(new Default2());
            currentChp = chapters.get(3);
            currentChp.check1();
            currentChp.check2();
         } 
      input.close();
    }
  }
    /*
       This is a developer testing tool that lets you skip to any chapter in the game to test it out.
    public void dRun(){
      Scanner input3 = new Scanner(System.in);
      System.out.println("What chp do you want to test?: (0 = Tutorial, 1 = Chp1, 2 = Ex chapter): ");
      String devInput = input3.nextLine();
      switch(devInput){
         case "0":
            currentChp = chapters.get(0);
            currentChp.check1();
            currentChp.check2();
            break;
         case "1":
            currentChp = chapters.get(1);
            currentChp.check1();
            currentChp.check2();
            break;
         case "2":
            currentChp = chapters.get(1);
            currentChp.devAggro(2);
            chapters.add(new Ex());
            currentChp = chapters.get(2);
            currentChp.check1();
            if (currentChp.check2() == true){
               System.out.println("");
               System.out.println("");
               System.out.println("");
               System.out.println("Thank you for playing! Restart the game to approach the game in an entirely new way!");
            }
            break;
         case "3":
            chapters.add(new Ex2());
            currentChp = chapters.get(2);
            currentChp.check1();
            currentChp.check2();
            break;
         case "4":
            chapters.add(new Default());
            currentChp = chapters.get(2);
            currentChp.check1();
            currentChp.check2();
            break;
         case "5":
            chapters.add(new Default2());
            currentChp = chapters.get(2);
            currentChp.check1();
            currentChp.check2();
            break;
         default:
            run();
            break;
      }
    }
    */
}
/*
   An abstract class chapter. this class serves as what a chapter can be. contains 3 methods that are already defined.
   
   Instance vars - all are protected to allow for use inside of the subclasses:
   
   rightPath - is turned false when the player is on the path for death (point of no return has been reached)
   input - the scanner used in each chapter
   chpNum - the number of the chapter *likely to be removed when optimizing the game
   pInput - the input of the player to make the code more optimized (using less ram by creating less instance vars)
   pInputFail - used for the logic of player interaction. Explained later on
*/
abstract class Chapter{
   protected static Scanner input;
   protected static String pInput;
   protected static boolean pInputFail;
   protected static ArrayList<Evidence> evidence;
   protected static int aggro;
   /*
      dPrint is essential to the game. Allows the text to be printed over time rather than instantaneously allowing the player to intreact with the game in a more pleasent manner.
      The method takes in a string and then prints out each letter one by one with a certain amount of delay
   */
   public void dPrint(String text){
      for(int i = 0; i < text.length(); i++){
          System.out.printf("%c", text.charAt(i));
          try{
              Thread.sleep(22);
          }catch(InterruptedException ex){
              Thread.currentThread().interrupt();
          }
      }
      System.out.println("");
   }
   /*
      A method that prints out all the evidence (that is in an arraylist) in an organized fashion.
   */
   public void ePrint(ArrayList<Evidence> arr){
      System.out.println("        EVIDENCE:");
      System.out.println("");
      for (int i = 0; i < arr.size(); i++){
         System.out.println(arr.get(i).toString());
      }
   }
   
   public void devAggro(int num){
      aggro = num;
   }
   
   /*
      Each chapter needs a minimum of 2 checkpoints. any more can be added within the respective class.
   */  
   abstract boolean check1();
   abstract boolean check2();
}
/*
   This is the tutorial chapter. it extends the chapter class
   contains the (you guessed it) tutorial of the game and how it functions.
   No unique instance vars are created because all are already present in the chapter abstract class.
*/
class Tutorial extends Chapter{
   /*
      Sets the right path var to true as the player should be doing things right at this point. 
      creates the new scanner object for the class.
      has an internal chpNum which is 0 for tutorial *Will likely be removed in a future build
      pInputFail - Used to monitor if the user put any input other than the ones accepted. Used in a loop that has a theoretical runtime of O(1). 
   */
   public Tutorial(){
      input = new Scanner(System.in);
      pInputFail = true;
   }
   /*
      first part of the tutorial. Explains how the game functions and introduces the user to the concept of a checkpoint (checkpoints may be removed if deemed uneessary)
   */
  @Override
   public boolean check1(){
      dPrint("\n                      Tutorial\n\n");
      dPrint("Welcome to the Puppeteer. This will serve as your guide on how to play through the adventure.\n");
      dPrint("After being prompted with some explanatory text, you will be asked what your next move is.\n");
      dPrint("Depending on your descions, the game will have multiple branching paths. \nIt is recommended that you play the game multiple times to get the full story as you will never get the full story in 1 playthrough.\n");
      dPrint("However, it seems you do not know how to make these descions. You will learn how to do so in the next part of the tutorial.\n");
      return true;
      }
   /*
      Many of the check blocks will look like this. A lot of text with many switches. The pIntputFail is used here to make sure the user answers before continuing on the journey. 
      In future chapters, there will be a method that leads to death if a certain choice is chosen and deviates from the main story. This will reset the user back to the prompt when they die.
   */
  @Override
   public boolean check2(){
      dPrint("Now I will prompt you with a 2 options. In order to proceed, you must input the number associated with Yes");
      dPrint("Would you like to continue? \n[1] Yes \n[2] No");
      while (pInputFail == true){
         System.out.print("Enter your answer here: ");
         pInput = input.nextLine();
         switch (pInput){
            case "1":
               pInputFail = false;
               break;
            case "2":
               System.out.println("Oops. you will continue anyways, right?"); 
               break;
            default:
               System.out.println("You entered something OTHER than the options provided. Please actually enter something in that works");
               break;
         }
      }
      pInputFail = true;
      dPrint("Very well. It seems you want to continue on this adventure. Let me double check my notes. OH! I forgot something really important.");
      dPrint("In this game, you will be provided with evidence that will be printed before every major block of text continuing the story. \nUse this evidence to your own benefit or completely ignore it. it is your choice");
      dPrint("Thank you for tolerating me in this tutorial. Enjoy the game Detective Alex Mercer.");
      return true;
   }
}
/*
   This class contains the blueprint for the first chapter of the game.
   The instance variables in this class are both private booleans and are used to further the story.
   Throughout the chapter, there are multiple private methods that are called depending on the user's choice. This is something consistent between each class to allow for more seperation of methods.
*/
class Chapter1 extends Chapter{
   private boolean didTalkToBF = false;
   private boolean bigInfo = false;
   public Chapter1(){
      input = new Scanner(System.in);
      pInputFail = true;
      aggro = 0; 
      evidence = new ArrayList<>();
      evidence.add(new Evidence("Melisa Mercer", "Your Wife", "Blonde Hair, medium stature. You met her at a nightclub when you worked in Philly."));
      evidence.add(new Evidence("Bob Derrick","Your wife's uncle","Works in the police department with you. Veteran of the department and is going to retire soon."));  
      evidence.add(new Evidence("Henry Derrick","Brother-in-Law","Also works in the department with you. Has a very Hank from breaking bad -esque attitude."));
   }
   @Override
   public boolean check1(){
      dPrint("\n              FRIDAY, DECEMBER 8th 2022 - 11:48pm - Your House\n");
      dPrint("It is the middle of the night. You wake up in a sweat. After trying to leave the bed, you notice your body has been mauled. You start to yell. \n\"AAAAAAAAAAAAAAAAAAAAAAH!\"");
      dPrint("This noise wakes you up. It was all a dream. You doze off for a little and eventually get woken up by your wife as she sees the insane amount of sweat on your body.\n");
      ePrint(evidence); 
      dPrint("\n                   CHAPTER 1 - The Examination\n");
      dPrint("You wake up the next day. \n RING RING! \nThat is your phone. You go to pick it up but notice something funky about the furniture in your room.");
      dPrint("What do you do? \n[1] Pickup the phone \n[2] Investigate your furniture");
      while (pInputFail == true){
         System.out.print("Enter your answer here: ");
         pInput = input.nextLine();
         switch(pInput){
            case "1":
               pInputFail = false;
               break;
            case "2":
               aggro++;
               furniture();
               pInputFail = false;
               break;
            default:
               System.out.println("Why did you not do anything? CHOOSE SOMETHING NOW!");
               break;
         }
      }
      pInputFail = true;
      dPrint("\"Hello?\"");
      if (aggro == 1 || aggro == 2){
         dPrint("Your wife walks into the room.");
      }
      dPrint("\"Hey alex. Looks like we got a murder. Head over to the address I just sent you. Looks like a realtor was killed.\"");
      dPrint("\"I'll be there soon.\"");
      if (aggro == 0){
         dPrint("Your wife walks into the room.");
      }
      dPrint("After you finish your call, your wife asks,\"Want to make us some waffles?\". \"Sorry honey. There was a murder in town. I have to go investigate.\" \n\"Oh no! Do you know who was killed?\" \n\"Based off of the case breif so far it looks like it was a realtor.\"");
      dPrint("\"Well that is unfortuante. I guess we will have some waffles at a later time. Stay safe Honey!\". You now leave the house");
      return true;
   }   
   @Override
   public boolean check2(){
      ePrint(evidence);
      System.out.println("");
      dPrint("              SATURDAY, DECEMBER 9th, 2022 - 10:24 AM - The Site of the Murder");
      System.out.println("");
      dPrint("You arrive at the home and enter it. You meet Bob Derrick. He starts to inform you on the case");
      dPrint("\"Man, this scene was gruesome. The victim is Laura Kliff and she was stabbed 45 times. No signs of defensive moves however. We also found some hair.\"");
      evidence.add(new Evidence("Laura Kliff", "Realtor", "Victim of the murder. No defensive wounds present on the body. Killed at around 2:00 am."));
      evidence.add(new Evidence("Unknown Hair", "Physical Evidence", "A long piece of blonde hair"));
      System.out.println("New Evidence Added! - Laura Kliff, Unknown Hair");
      dPrint("\"Any info on her current relationship? Seems really personal given the... overkill aproach to the murder.\"");
      dPrint("\"The guy she is in a relationship with seems clean. No dirt on his record. Their relationship was in a good condition.\"");
      dPrint("\"Is there any possibility for me to talk with him?\"");
      dPrint("\"Sure. Let me know if you want to meet him.\"");
      dPrint("\nWould you like to meet with the boyfriend? \n[1] Yes \n[2] No");
      System.out.println("");
      while (pInputFail == true){
         System.out.print("Enter your answer here: ");
         pInput = input.nextLine();
          switch (pInput) {
            case "1" -> {
               aggro++;
               dPrint("Sure. Set up a meeting with him for me later today.");
               bfTalk();
               pInputFail = false;
            }
            case "2" -> {
               dPrint("\"I think I will pass.\"");
               pInputFail = false;
            }
            default -> System.out.println("Input not valid. try again");
          }
      }
      pInputFail = true;
      if (didTalkToBF == true && bigInfo == false){
         dPrint("You escort Justin out and begin to look more into the case file."); 
         return false;
      }
      if (bigInfo == true){
         dPrint("After learning about this \"friend\", you tell Bob about it and eventually figure out the location of the home using Laura's cell location.");
         return true;
      }
      dPrint("\"Hey Bob, I'm heading back to the precinct to look at the case file and to do some research.\"");
      dPrint("\"Sounds great. See ya at the precinct alex.\"");
      return false;
   }
   
   private void bfTalk(){
      didTalkToBF = true;
      evidence.add(new Evidence("Justin Timber", "The Boyfriend", "Medium Stature. Also a realtor. Blonde Hair."));
      System.out.println("");
      ePrint(evidence);
      System.out.println("");
      dPrint("           Later that day at the police precinct...");
      System.out.println("");
      dPrint("\"Hello there Mr. Timber. Thank you for coming in to speak with me.\"");
      dPrint("\"I will do anything to help find who did this to Laura.\"");
      dPrint("\"Ok. So to start, go through your day with me. If you woke up really early for some reason, let me know. It is important you give me all the deets.\"");
      dPrint("\"So, I woke up around 4:30 in the morning to go on a run. Laura told me she would be out for the night staying at a friends house.\nAfter I came back from the run, I showered and slept until 8:30ish. That is when....\nI gg...g..got the c...c.ccc.ccc.call.\"");
      System.out.println("");
      dPrint("Justin breaks and begins to cry aggresively. You console him.");
      System.out.println("");
      dPrint("\"Thank you for all this information. It is helping the case a lot. Can you please tell me more about what you remember. Maybe more specifics?\"");
      aggro++;
      bfTalk2();
      pInputFail = false;
   } 
   
   private void bfTalk2(){
      evidence.get(4).destroy();
      dPrint("\"The friend she said she was going to visit lives in a small fishing town outside of the city. You can check her phone location to see where she went.\"");
      dPrint("\"Thank you very much Justin. This extra information you gave me is awesome.\" \n\"No problem.\"");
      System.out.println("");
      ePrint(evidence);
      System.out.println("");
      bigInfo = true;
   }
   
   private void furniture(){
      dPrint("As you walk up to you drawers, you notice that it is not flush with the wall. \nYou hear your wife walking down the hallway to your room. What do you do? \n[1] Investigate further \n[2] Brush it off");
      while (pInputFail == true){
         System.out.print("Enter your answer here: ");
         pInput = input.nextLine();
         switch(pInput){
            case "1":
               aggro++;
               furniture2();
               pInputFail = false;
               break;
            case "2":
               dPrint("Your phone rings again and you proceed to pick up.");
               pInputFail = false;
               break;
            default:
               System.out.println("Input not valid. try again");
               break;
          }
      }
   }
   private void furniture2(){
      dPrint("As you look behind your drawers, you notice something weird. The wall's coloring is not consistent, as if some damage had been done to it. \nYour wife is getting closer and closer to the room and you also get a phone call. What do you do? \n[1] Continue inspecting and ignore everything\n[2] Pick up the phone");
      while (pInputFail == true){
         System.out.print("Enter your answer here: ");
         pInput = input.nextLine();
         switch(pInput){
            case "1":
               aggro = aggro + 3;
               dPrint("As your rip out that part of the wall, you see nothing. Your wife then walks in and yells \"WHAT ARE YOU DOING? WE JUST BOUGHT THIS HOME!\" \n\"Sorry honey, I noticed something funky and it was irritating me.\"");
               dPrint("Your wife then says, \"Let me see what is wrong with the wall.... oh. that is odd. Probably a remnant from the old owner.\"");
               dPrint("Your phone then rings and you pick it up");
               pInputFail = false;
               break;
            case "2":
               pInputFail = false;
               break;
         }
      }
   }
}

class Default extends Chapter{
   public static boolean bInvestigate; 
   
   public Default(){
      bInvestigate = false; 
      pInputFail = true;
   }
   @Override
   public boolean check1(){
      System.out.println("");
      dPrint("             The File - SATURDAY, DECEMBER 9th, 2022 - 12:00 PM - The Police Precinct");
      System.out.println("");
      dPrint("You return to the precinct and begin to look more into the case and the people Laura interacted with.");
      dPrint("You do the routine background checks, and find something odd - She has been maried before.");
      dPrint("Your mind immediately goes to one thing - Revenge Murder");
      dPrint("As you make this discovery, Henry Derrick walks in and tells you that it is time to go home (it is now 4:00 PM) and celebrate Henry's wife's birthday.");
      dPrint("You have two options: \n[1] Leave with Henry \n[2] Stick around to look through the case file more.");
      while(pInputFail == true){
         System.out.print("Enter your answer here: ");
         pInput = input.nextLine();
         switch(pInput){
            case "1":
               dPrint("\"Sure. Lets go celebrate.\"");
               autopsy();
               pInputFail = false;
               break;
            case "2":
               dPrint("\"I think i'm gonna investigate the file a bit more. Just found something important.\"");
               dPrint("\"Aww man. That's fine. Hopefully you will be able to join us after you finish with the case. Have a good day Alex.\"");
               investigateEx();
               pInputFail = false;
               break;
            default:
               System.out.println("Input not valid. try again");
               break;
         }
      }
      pInputFail = true;
      return false;
   }
   
   private void investigateEx(){
      dPrint("After saying goodbye to Henry, you continue to look into her ex-husband.");
      dPrint("You end up finding his address and recognize the area from somehwere.");
      dPrint("When you check the cell towers that Laura's phone pinged, you see that she pinged one in that area. \nWith this new information, you decide that you will go to the home to ask him some questions.");
      bInvestigate = true;
   }
   
   private void autopsy(){
      dPrint("After partying the entire night with your family, you return to the precinct the next day and get a call saying the autopsy has been done on the body.");
      dPrint("You then drive to the morgue.");
      dPrint("When you arrive, you are invited into the operation room by the mortician who gives you the overview on what happened.");
      dPrint("\"Hello there Alex. Looks like you are up early and a little bit hungover.\"");
      dPrint("\"Tell me about it. Spent the whole night partying with the family.\"");
      dPrint("\"Good to hear. Now for the autopsy report.\"");
      dPrint("\"I'm going to skip over most of the stuff you already know and that is obvious.\"");
      dPrint("\"As for noteworthy things, we found evidence that Laura had engaged in sexual acitvity prior to her death, which brings in the possibility of rape.\"");
      dPrint("\"However, there are no signs of sexual assault which is really preplexing.\"");
      dPrint("\"Also we were unable to get a good sample of the DNA becuase it has been destroyed by something.\"");
      dPrint("\"Huh. That is very odd.\"");
      dPrint("\"BUT, this is explained by the toxicology report. In her blood, we found extremely high amounts of heroin and a really weired find of Radionuclides.\"");
      dPrint("\"Radionuclides are usually used in rare instances during radiation therapy, so it is weird to see them present in her body. \nIt also explains the damage to DNA matter as radioactivity causes damage to DNA.\"");
      dPrint("\"Wow. That was some really useful info. Thanks a lot.\"");
      dPrint("With this new information, all suspicion points towards Justin Timber.");
   }
   @Override
   public boolean check2(){
      return bInvestigate;
   }
}

class Default2 extends Chapter{
   public Default2(){
      pInputFail = true;   
   }
   @Override
   public boolean check1(){
      dPrint("         S a d n e s s   -  Saturday, December 9th, 2023 - 11:00 PM");
      dPrint("As you look into Timber Real Estate's past sales, they have all been considered abandonded 15 months after sale except for a few.");
      dPrint("When you check what was found in each building, you see that a major commonality was the prescence of drugs.");
      dPrint("Why was this being hidden by the police?");
      dPrint("With this knowledge, you go to a confidant of yours who knows the family really well and tell him all about your discovery.");
      dPrint("He asks you more and more questions about what you know and tells you to meet with him tommorow to discuss further.");
      dPrint("When you return home 43 minutes later, you get ready for bed, and as you sleep, you wake with your body completely mauled.");
      dPrint("I T   W A S    N E V E R    A    D R E A M");
      System.out.println("");
      System.out.println("");
      System.out.println("");
      System.out.println("");
      System.out.println("The End");
      System.out.println("");
      System.out.println("");
      System.out.println("Thank you for playing! Restart the game to try and get a different ending!");
      return true;
   }
   @Override
   public boolean check2(){
      return true;
   }
}

/*
   One of the two branching paths that can occur from Chapter 1. If you decided to meet with the boyfriend, you are automatically placed into this storyline.
   However, if you did not talk to the boyfriend but made the right choices in the Default chapter, the story can end up here.
*/
class Ex extends Chapter{
   private boolean highAggro;
   private boolean majorFup;
   
   public Ex(){
      if (aggro > 4){
         highAggro = true;
      }
      else {
         highAggro = false;
      }
      if(Default.bInvestigate == true){
         dPrint("Unforunately, you missed th part as it ended early.");
      }
      System.out.println("");
      ePrint(evidence);
      System.out.println("");
      dPrint("         The Ex - SATURDAY DECEMBER 9th, 2023 - A new discovery");
      System.out.println("");
   }
   
   public boolean check1(){
      while (pInputFail == true){
         dPrint("After all this new information, you can either go home right now and travel to the friend the next day or go right now. What will you do?: \n[1] Travel now\n[2] Stay home for the night");
         System.out.print("Enter your answer here: ");
         pInput = input.nextLine();
         switch(pInput){
            case "1":
               travelNow();
               pInputFail = false;
               break;
            case "2":
               if(highAggro == true){
                  homeDeath();
                  check1();
               }
               else{
                  dPrint("When you arrive home, you spend some quality time with your wife and then go out to eat dinner. However, you notice someone that you recognize, but since you are extremely drunk, you aren't able to fully recognize him.\nYou then come home and sleep and then wake up the next day.");
                  travelLater();
                  pInputFail = false;
               }
               break;
            default:
               System.out.println("Input not valid. try again");
         }
      }
      
      return true;
   }
   @Override
   public boolean check2(){
      return majorFup;
   }
   
   private void homeDeath(){
      dPrint("When you return home, you take a quick nap. You and your wife then decide to go out for a dinner date. \nYou have a fun time with her, drinking a lot. You also notice a guy who you've seen today with blonde hair.\nSuddenly, you begin to feel lightheaded.\nYour vision becomes smaller and smaller... \nuntil               \nthe                 \nsweet                  \nrelief           \nof                                              \ndeath.");
      System.out.println("");
      dPrint("Looks like you found out :)- M");
      System.out.println("");
      System.out.println("Restarting from checkpoint...");
      System.out.println("");
   }

   
   private void travelNow(){
      dPrint("After leaving the precinct, you call your wife informing her of your travel");
      dPrint("On the way to the home, it is getting dark and you find a person in a trenchcoat stranded on the side of the road with their car not working. What do you do? \n[1] Help this person \n[2] Ignore them");
      while(pInputFail == true){
         dPrint("On the way to the home, it is getting dark and you find a person in a trenchcoat stranded on the side of the road with their car not working. What do you do? \n[1] Help this person \n[2] Ignore them");
         System.out.print("Enter your answer here: ");
         pInput = input.nextLine();
         switch(pInput){
            case "1":
               dPrint("After pulling up to the side of the road, you get out of your car and ask the person if he needs help. \nThis person looks familiar, almost as if you had seen him earlier that day but you can't fully recognize him as it is late at night.");
               dPrint("He tells you that his car is not starting and needs a jump from your car.");
               dPrint("You then start walking back to your car.\nYou hear a lound boom and are greeted by death.");
               System.out.println("");
               dPrint("Restarting from checkpoint...");
               System.out.println("");
               break;
            case "2":
               pInputFail = false;
               dPrint("You continue on your merry way feeling a little bit guilty for leaving this person on the side of the road alone.");
               break;
            default:
               System.out.println("Input not valid. try again");
               break;
         }
      }
      pInputFail = true;
      dPrint("As you arrive at the home, you start to become nervous and have your gun at the ready.");
      dPrint("You pull up the front door and knock.");
      dPrint("A shaggy, brown-haired man opens the door. He asks, \"Who are you?\"");
      dPrint("\"Alex Mercer of Montasville PD. Checking in on a case. Do you happen to know who Laura Kliff is?\"");
      dPrint("\"Why do you ask?\"");
      dPrint("\"Well, she has gone missing and her last phone location was around here.\"");
      dPrint("You start to see him reach for his back pocket. What do you do? \n[1] Pull out your gun \n[2] Ignore it");
      while(pInputFail == true){
         System.out.print("Enter your answer here: ");
         pInput = input.nextLine();
         switch(pInput){
            case "1":
               dPrint("As his hand pulls out, you see a metalic object. BOOM");
               dPrint("You shot your gun. The man is now on the floor bleeding. The, he dies.");
               dPrint("As you walk into the home, you don't see anything on the floor. The metalic thing you saw was just a ring.");
               dPrint("You then call in backup informing them of the murder");
               fUp();
               pInputFail = false;
               break;  
            case "2":
               dPrint("As his hand comes into view, there is nothing in it. You made the right choice.");
               dPrint("\"Is it possible for you to tell me what your relationship with Laura was?\"");
               dPrint("\"I'm her ex-husband. Mark Carlo\"");
               dPrint("\"Can you please tell me what she was doing here last night?\"");
               dPrint("\"Well, we have been back together for a bit but not seriously.\"");
               dPrint("\"Can you tell me where you were last night?\"");
               dPrint("\"I was home. Laura left at around 5 PM last night after we slept together.\"");
               dPrint("\"Thank you very much. Have a great night Mark.\"");
               dPrint("As you begin to leave, you notice a blonde wig in his home along with some sleeping pills.");
               evidence.add(new Evidence("Mark Carlo","Victim's Ex-husband","Victim appears to be cheating on her boyfriend with him. Says Laura left his home at 5 PM, 9 hours before the murder."));
               dPrint("You then make your way home and sleep till the next day.");
               pInputFail = false;
               break;
            default:
               System.out.println("Input not valid. try again");
               break;
         }
      }
      pInputFail = true;
   }
   
   private void travelLater(){
      dPrint("After waking up, you inform your wife of your travels.");
      dPrint("On the way to the home, you find a person in a trenchcoat stranded on the side of the road with their car not working. What do you do? \n[1] Help this person \n[2] Ignore them");
      while(pInputFail == true){
         dPrint("On the way to the home, you find a person in a trenchcoat stranded on the side of the road with their car not working. What do you do? \n[1] Help this person \n[2] Ignore them");
         System.out.print("Enter your answer here: ");
         pInput = input.nextLine();
         switch(pInput){
            case "1":
               dPrint("After pulling up to the side of the road, you get out of your car and ask the person if he needs help. \nThis person looks familiar, almost as if you had seen him lately but you can't fully recognize him because he is wearing sunglasses and a hat");
               dPrint("He tells you that his car is not starting and needs a jump from your car.");
               dPrint("You then start walking back to your car.\nYou hear a lound boom and are greeted by death.");
               System.out.println("");
               dPrint("Restarting from checkpoint...");
               System.out.println("");
               break;
            case "2":
               pInputFail = false;
               dPrint("You continue on your merry way feeling a little bit guilty for leaving this person on the side of the road alone.");
               break;
            default:
               System.out.println("Input not valid. try again");
               break;
         }
      }
      pInputFail = true;
      dPrint("As you arrive at the home, you start to become nervous and have your gun at the ready.");
      dPrint("You pull up the front door and knock.");
      dPrint("A shaggy, brown-haired man opens the door. He asks, \"Who are you?\"");
      dPrint("\"Alex Mercer of Montasville PD. Checking in on a case. Do you happen to know who Laura Kliff is?\"");
      dPrint("\"Why do you ask?\"");
      dPrint("\"Well, she has gone missing and her last phone location was around here.\"");
      dPrint("You start to see him reach for his back pocket. What do you do? \n[1] Pull out your gun \n[2] Ignore it");
      while(pInputFail == true){
         System.out.print("Enter your answer here: ");
         pInput = input.nextLine();
         switch(pInput){
            case "1":
               dPrint("As his hand pulls out, you see a metalic object. BOOM");
               dPrint("You shot your gun. The man is now on the floor bleeding. The, he dies.");
               dPrint("As you walk into the home, you don't see anything on the floor. The metalic thing you saw was just a ring.");
               dPrint("You then call in backup informing them of the murder");
               fUp();
               pInputFail = false;
               break;  
            case "2":
               dPrint("As his hand comes into view, there is nothing in it. You made the right choice.");
               dPrint("\"Is it possible for you to tell me what your relationship with Laura was?\"");
               dPrint("\"I'm her ex-husband. Mark Carlo\"");
               dPrint("\"Can you please tell me what she was doing here last night?\"");
               dPrint("\"Well, we have been back together for a bit but not seriously.\"");
               dPrint("\"Can you tell me where you were last night?\"");
               dPrint("\"I was home. Laura left at around 5 PM last night after we slept together.\"");
               dPrint("\"Thank you very much. Have a great night Mark.\"");
               dPrint("As you begin to leave, you notice a blonde wig in his home along with some sleeping pills.");
               evidence.add(new Evidence("Mark Carlo","Victim's Ex-husband","Victim appears to be cheating on her boyfriend with him. Says Laura left his home at 5 PM, 9 hours before the murder."));
               pInputFail = false;
               break;
            default:
               System.out.println("Input not valid. try again");
               break;
         }
      }
      pInputFail = true;

   }
   private void fUp(){
      majorFup = true; 
      dPrint("When backup comes and your inform them of what happened, you are placed under arrest.");
      dPrint("After the investigation, the city prosecutor decides to press charges against you for 3rd degree murder.");
      dPrint("You are ultimately sentenced to 20 years in prision. Your wife leaves you. You eventually die in prision, all alone, unable to find out who really killed Laura Kliff.");
      System.out.println("");
      dPrint("The End");
   }
}
/*
   A continuation of the Ex branching path. You ultimately are able to figure out who killed Laura and why and you die (a common theme) using the info from Mark.
   This is one of the two final chapters.
*/
class Ex2 extends Chapter{
   private boolean dCarlo;
   private boolean dTimber;
   private boolean figured;
   public Ex2(){
      dPrint("With this new info in mind, you come back to the precinct and scour through the case files.");
      figured = false;
      dCarlo = false;
      dTimber = false;
   }
   
   public boolean check1(){
      pInputFail = true;
      System.out.println("");
      dPrint("Something seems off. With all this blonde hair going around, you decide to check the evidence again.");
      System.out.println("");
      ePrint(evidence);
      System.out.println("");
      dPrint("Wait, was this blonde hair a figment of your imagination? and what is up with this haunting message in the evidence?");
      dPrint("This concerns you, and as you are looking through the file after looking at the evidence, Laura's toxicology report comes out.");
      dPrint("In this report, it reveals that Laura had been taking heroin.");
      dPrint("This ultimately opened your eyes to a massive realization: this kill had to do with drugs and the two people that are of great intrest to you.");
      dPrint("You have two options to continue. Do you: \n \n[1] Investigate Justin Timber's Real Estate buisness \n[2] Investigate Mark Carlo's record further");
      while (pInputFail == true){
         System.out.print("Enter your answer here: ");
         pInput = input.nextLine();
         switch(pInput){
            case "1":
               timber();
               pInputFail = false;
               break;
            case "2":
               carlo();
               pInputFail = false;
               break;
            default:
               System.out.println("Input not valid. try again");
               break;
         }
         
      }
      return false;
   }
   
   private void timber(){
      dTimber = true;
      dPrint("As you look into Timber Real Estate's past sales, they have all been considered abandonded 15 months after sale except for a few.");
      dPrint("When you check what was found in each building, you see that a major commonality was the prescence of drugs.");
      dPrint("Why was this being hidden by the police?");
      dPrint("You have an option: Do you \n[1] Inquire further \n[2] Shrug it off");
      while(pInputFail == true){
         System.out.print("Enter your answer here: ");
         pInput = input.nextLine();
         switch(pInput){
            case "1":
               dPrint("As you inquire further, you figure out that they officers on these cases were always your brother-in-law and your wife's uncle.");
               dPrint("You feel broken. People you have grown to love turn out to be... bad");
               figured = true;
               pInputFail = false;
               break;
            case "2":
               dPrint("With is information in mind, you decide to bring up a case against Justin Timber, charging him with the murder of Laura and drug trafficking.");
               pInputFail = false;
               break;
            default:
               System.out.println("Input not valid. try again");
         }
      }
   }
   
   private void carlo(){
      dCarlo = true;
      dPrint("As you look into Carlo's criminal history, you notice a multitude of drug charges for possesion.");
      dPrint("This all checks out when you remember the way Mark looked.");
      dPrint("When you look into his sentences however, you notice that he got the minimum punishment allowable by law which was the equivalent to a slap on the wrist.");
      dPrint("You have an option: Do you \n[1] Inquire further \n[2] Shrug it off");
      while(pInputFail == true){
         System.out.print("Enter your answer here: ");
         pInput = input.nextLine();
         switch(pInput){
            case "1":
               dPrint("As you inquire further, you figure out that they officers on these cases were always your brother-in-law and your wife's uncle.");
               dPrint("You feel broken. People you have grown to love turn out to be... bad");
               figured = true;
               pInputFail = false;
               break;
            case "2":
               dPrint("With is information in mind, you decide to bring up a case against Mark Carlo, charging him with the murder of Laura and possesion of drugs.");
               pInputFail = false;
               break;
            default:
               System.out.println("Input not valid. try again");
         }
      }

   }
   
   public boolean check2(){
      if(dCarlo == true && figured == false){
         dPrint("After investigating further, you are able to form a strong case against Mark Carlo.");
         dPrint("You then give the case to a prosector who is able to successfully convict Mark of these crimes.");
         dPrint("Drug activity in the town eventually slowed down.");
         dPrint("This, however, was short lived.");
         dPrint("Because of that, you still think to yourself, did I make the right choice?");
         System.out.println("");
         System.out.println("");
         System.out.println("");
         System.out.println("");
         System.out.println("The End");
         System.out.println("");
         System.out.println("");
         System.out.println("Thank you for playing! Resart the game to try and get a different ending!");
      }
      else if(figured == true){
         dPrint("With this knowledge, you go to a confidant of yours who knows the family really well and tell him all about your discovery.");
         dPrint("He asks you more and more questions about what you know and tells you to meet with him tommorow to discuss further.");
         dPrint("When you return home, you get ready for bed, and as you sleep, you wake with your body completely mauled. \nWith your wife standing next to you with bloody hands and a knife in hand. Your confidant was a rat.");
         dPrint("I T   W A S    N E V E R    A    D R E A M");
         System.out.println("");
         System.out.println("");
         System.out.println("");
         System.out.println("");
         System.out.println("The End");
         System.out.println("");
         System.out.println("");
         System.out.println("Thank you for playing! Restart the game to try and get a different ending!");
      }
      else if(dTimber == true && figured == false){
         dPrint("When you investigate further, you figure out that this corruption runs deep. You also figure out one crushing fact: Your wife has been sleeping with Justin.");
         dPrint("As a result, you end up divorcing your wife and are unable to bring this trial to court due to the personal involement in the investigation.");
         dPrint("When you are at a restaurant one day having your meal outside, an ominous car pulls up in front of you and you are gunned down?");
         dPrint("Would ignorance have been better?");
         System.out.println("");
         System.out.println("");
         System.out.println("");
         System.out.println("");
         System.out.println("The End");
         System.out.println("");
         System.out.println("");
         System.out.println("Thank you for playing! Restart the game to try and get a different ending!");
      }
      return false;
   }
}
/*
   Contains all the data for any evidence provided to the player
   
   Instance vars:
   
   name - the name of the evidence
   type - the type of the evidence (autopsy, physical evidence, cell tower pings)
   description - the information contained by the evidence
*/
class Evidence{
   private String name;
   private String type;
   private String description;
   
   public Evidence(String nom, String tp, String descrp){
      name = nom;
      type = tp;
      description = descrp;
   }
   
   public String toString(){
      return "Name: " + name + "       Type: " + type + "    Description: " + description;
   }
   /*
      In certain points of the story, evidence will be destroyed providing a subtle hint as to who could be the true bad buy *ahem* *ahem* the wife when she destroys some physical evidence of hair.
   */
   public void destroy(){
      name = "Tick.Tock.Tick.Tock.Your.Time.Is.Almost.Up";
      description = "Stop";
   }
}