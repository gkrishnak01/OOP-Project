package gkrishnak;

import java.util.Scanner;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.Iterator;

class ForSort // Used for sorting the scores to be inserted to results.json
{
    String ppant;
    long score;
    long position;

    ForSort(String ppant,long score)
    {
        this.ppant = ppant;
        this.score = score;
    }
}

class Person
{
    String personID;
    String personName;
    String mobileNo;
    String address;

    Person()
    {}

    Person(String personID,String personName, String mobileNo,String address)
    {
        this.personID = personID;
        this.personName = personName;
        this.mobileNo = mobileNo;
        this.address = address;
    }

}

class Incharge extends Person
{
    Incharge(String personID,String personName, String mobileNo,String address)
    {
        super(personID,personName,mobileNo,address);
    }

    public void insertIncharge() //Adds details to incharge.json
    {
        JSONObject inchargeObject = new JSONObject();
        JSONParser parser = new JSONParser();  

        try
        {
            inchargeObject.put("Incharge Name",personName); //Adds to the object to be nested
            inchargeObject.put("Mobile No",mobileNo);
            inchargeObject.put("Address",address);

            JSONObject incharges = (JSONObject) parser.parse(new FileReader("incharge.json"));
            incharges.put(personID, inchargeObject); // Adds the nested one to the main one
            
            FileWriter file = new FileWriter("incharge.json");
            file.write(incharges.toJSONString()); 
            file.close();
        }catch (ParseException | IOException e) 
        {
            e.printStackTrace();
        }
    }
}

class Judge extends Person
{
    Judge(String personID,String personName, String mobileNo,String address)
    {
        super(personID,personName,mobileNo,address);
    }

    public void insertJudge() //Adds details to judge.json
    {
        JSONObject judgeObject = new JSONObject();
        JSONParser parser = new JSONParser();  

        try
        {
            judgeObject.put("Judge Name",personName);
            judgeObject.put("Mobile No",mobileNo);
            judgeObject.put("Address",address);

            JSONObject judges = (JSONObject) parser.parse(new FileReader("judge.json"));
            judges.put(personID, judgeObject);
            
            FileWriter file = new FileWriter("judge.json");
            file.write(judges.toJSONString()); 
            file.close();
        }catch (ParseException | IOException e) 
        {
            e.printStackTrace();
        }
    }
}

class Participants extends Person
{
    JSONObject participatingContests = new JSONObject();

    Participants(String personID,String personName, String mobileNo,String address)
    {
        super(personID,personName,mobileNo,address);
    }

    public void addParticipating(String contest) // When user select a contest it is added to this. 0 is the initial score
    {
        participatingContests.put(contest,0);
    }

    public void insertParticipant()
    {
        JSONObject participantObject = new JSONObject();
        JSONParser parser = new JSONParser();  

        try
        {
            participantObject.put("Participant Name",personName);
            participantObject.put("Mobile No",mobileNo);
            participantObject.put("Address",address);

            JSONObject participants = (JSONObject) parser.parse(new FileReader("participants.json"));
            participants.put(personID, participantObject);
            
            FileWriter file = new FileWriter("participants.json");
            file.write(participants.toJSONString()); 
            file.close();
            
            JSONObject read = (JSONObject) parser.parse(new FileReader("scoring.json"));
            
            for(Iterator iterator = participatingContests.keySet().iterator(); iterator.hasNext();) //Inserts the participating contests to scoring.json
            {
                String key = (String) iterator.next();
                
                JSONObject individualContest = (JSONObject) read.get(key);
                individualContest.put(personID, 0);
                
                read.put(key, individualContest);

                FileWriter file2 = new FileWriter("scoring.json");
                file2.write(read.toJSONString()); 
                file2.close(); 
            }
        }catch (ParseException | IOException e) 
        {
            e.printStackTrace();
        }
    }
}

class Venue
{
    String venueID;
    String venueName;
    String venueLocation;

    Venue(String venueID, String venueName, String venueLocation)
    {
        this.venueID = venueID;
        this.venueName = venueName;
        this.venueLocation = venueLocation;
    }

    void insertVenue() //Adds venue details to venue.json
    {
        JSONObject venueObject = new JSONObject();
        JSONParser parser = new JSONParser();  

        try
        {
            venueObject.put("Venue Name",venueName);
            venueObject.put("Location",venueLocation);

            JSONObject venues = (JSONObject) parser.parse(new FileReader("venue.json"));
            venues.put(venueID, venueObject);
            
            FileWriter file = new FileWriter("venue.json");
            file.write(venues.toJSONString()); 
            file.close();
        } catch (ParseException | IOException e) 
        {
            e.printStackTrace();
        }
    }
}

class Contests 
{
    String contestID;
    String contestName;
    String inChargeId;
    String venueID;
    String date;
    String time;

    Contests(String contestID, String contestName, String inChargeId,String venueID, String date, String time)
    {
        this.contestID = contestID;
        this.contestName = contestName;
        this.inChargeId = inChargeId;
        this.venueID = venueID;
        this.date = date;
        this.time = time;
    }
    
    void insertContest()
    {
        JSONObject contestObject = new JSONObject();
        JSONParser parser = new JSONParser();
 
        try
        {
            contestObject.put("Contest Name",contestName);
            contestObject.put("Incharge ID",inChargeId);
            contestObject.put("Venue ID",venueID);
            contestObject.put("date",date);
            contestObject.put("time",time);
               
            JSONObject contests = (JSONObject) parser.parse(new FileReader("contests.json"));
            contests.put(contestID, contestObject);

            FileWriter file = new FileWriter("contests.json");
            file.write(contests.toJSONString()); 
            file.close();

            JSONObject scores = (JSONObject) parser.parse (new FileReader("scoring.json"));
            JSONObject scoresIndividual = new JSONObject();
            
            scores.put(contestID, scoresIndividual);

            FileWriter file2 = new FileWriter("scoring.json");
            file2.write(scores.toJSONString()); 
            file2.close();
        } catch (ParseException| IOException e) 
        {
            e.printStackTrace();
        }
    }

}

public class App
{
    static void getParticipantList()
    {

        try {
            JSONParser parser = new JSONParser();
            JSONObject a = (JSONObject) parser.parse(new FileReader("participants.json"));
            
            System.out.println("ID\t\tName\t\tMobile No");
            for(Iterator iterator = a.keySet().iterator(); iterator.hasNext();) 
            {
                String key = (String) iterator.next();
                JSONObject individJsonObject = (JSONObject) a.get(key);

                System.out.print(key+"\t\t");
                System.out.print(individJsonObject.get("Participant Name")+"\t\t");
                System.out.println(individJsonObject.get("Mobile No"));
            }
        } catch (Exception e) 
        {
            e.printStackTrace();
        } 
    }

    static void getSchedule()
    {
        try 
        {
            JSONParser parser = new JSONParser();  
            JSONObject a = (JSONObject) parser.parse(new FileReader("contests.json"));
            JSONObject b = (JSONObject) parser.parse(new FileReader("venue.json"));

            System.out.println("Contest\tVenue\tDate\tTime");

            for(Iterator iterator = a.keySet().iterator(); iterator.hasNext();) 
            {
                String key = (String) iterator.next();

                JSONObject indi = (JSONObject) a.get(key);
                JSONObject getVenue = (JSONObject) b.get(indi.get("Venue ID"));

                System.out.print(indi.get("Contest Name")+"\t");
                System.out.print(getVenue.get("Venue Name")+"\t");
                System.out.print(indi.get("date")+"\t");
                System.out.println(indi.get("time"));
            }   
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    static void setScore(String pid,String cid,int score)
    {
        try 
        {
            JSONParser parser = new JSONParser();  
            JSONObject scores = (JSONObject) parser.parse(new FileReader("scoring.json"));
            JSONObject contests = (JSONObject) scores.get(cid);
            contests.put(pid, score);
            scores.put(cid,contests);

            FileWriter file = new FileWriter("scoring.json");
            file.write(scores.toJSONString()); 
            file.close();
            
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    static void getIndiScore(String pid, String cid)
    {
        try
        {
            JSONParser parser = new JSONParser();  
            JSONObject a = (JSONObject) parser.parse(new FileReader("scoring.json"));
            JSONObject b = (JSONObject) a.get(cid);
            
            System.out.println("Score :"+b.get(pid));   
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    static void publishResult()
    {
        try 
        {
            JSONParser parser = new JSONParser();  
            JSONObject scores = (JSONObject) parser.parse(new FileReader("scoring.json"));

            for(Iterator iterator = scores.keySet().iterator(); iterator.hasNext();) 
            {
                String key = (String) iterator.next();
            

                JSONObject scoreIndividual = (JSONObject) scores.get(key);

                ForSort pos[] = new ForSort[scoreIndividual.size()]; //Assigning to FORSORt for sorting
                int index = 0;
                for(Iterator iterator2 = scoreIndividual.keySet().iterator(); iterator2.hasNext();) 
                {
                    String key2 = (String) iterator2.next();
                    pos[index] = new ForSort(key2, (long) scoreIndividual.get(key2));
                    index += 1;

                }   
                
                for(int i = 0; i < scoreIndividual.size() - 1 ; i++) //Sorting
                {
                    int max_idx = i; 
                    for (int j = i+1; j < scoreIndividual.size(); j++) 
                        if (pos[j].score > pos[max_idx].score) 
                            max_idx = j; 
        
                    ForSort temp = pos[max_idx]; 
                    pos[max_idx] = pos[i]; 
                    pos[i] = temp; 
                } 
            
                JSONObject results = (JSONObject) parser.parse (new FileReader("results.json"));
                JSONObject resultIndividual = new JSONObject();
                
                for(int q = 0; q < 3 && q < pos.length ; q ++)
                {
                    resultIndividual.put(q+1,pos[q].ppant);
                }

                results.put(key, resultIndividual);
                
                FileWriter file = new FileWriter("results.json");
                file.write(results.toJSONString()); 
                file.close(); 
            }
            System.out.println("Result Published");
        }catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    static void viewResult()
    {
        try 
        {
            JSONParser parser = new JSONParser();
            JSONObject res = (JSONObject) parser.parse (new FileReader("results.json"));
            JSONObject cont = (JSONObject) parser.parse(new FileReader ("contests.json"));
            JSONObject participant = (JSONObject) parser.parse(new FileReader ("participants.json"));

            for(Iterator iterator = res.keySet().iterator(); iterator.hasNext();) 
            {
                String key = (String) iterator.next();
                JSONObject contestIndvidual = (JSONObject) cont.get(key);
                System.out.println("Contests :"+contestIndvidual.get("Contest Name"));
                System.out.println("Pos"+"\t"+"Participant");
                JSONObject resultEachContests = (JSONObject) res.get(key);

                for(Iterator iterator2 = resultEachContests.keySet().iterator(); iterator2.hasNext();) 
                {
                    String key2 = (String) iterator2.next();
                    JSONObject particpantIndiObject = (JSONObject) participant.get(resultEachContests.get(key2));
                    System.out.println(key2 +"\t"+ particpantIndiObject.get("Participant Name"));  
                }    
            } 
        }catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    static void addIncharge()
    {
        Scanner input = new Scanner(System.in);

        String personID;
        String personName;
        String mobileNo;
        String address;

        System.out.print("Enter Incharge ID:");
        personID = input.nextLine();
        System.out.print("Enter Incharge Name:");
        personName = input.nextLine();
        System.out.print("Enter mobile No:");
        mobileNo = input.nextLine();
        System.out.print("Enter address:");
        address = input.nextLine();

        Incharge inc = new Incharge(personID,personName,mobileNo,address);
        inc.insertIncharge();

        input.close();
    }

    static void addVenue()
    {
        Scanner input = new Scanner(System.in);

        String venueID;
        String venueName;
        String venueLocation;

        System.out.print("Enter venue ID:");
        venueID = input.nextLine();
        System.out.print("Enter venue Name:");
        venueName = input.nextLine();
        System.out.print("Enter venue Location:");
        venueLocation = input.nextLine();

        Venue ven = new Venue(venueID,venueName,venueLocation);

        ven.insertVenue();
        input.close();
        
    }

    static void addJudge()
    {
        Scanner input = new Scanner(System.in);

        String personID;
        String personName;
        String mobileNo;
        String address;

        System.out.print("Enter Judge ID:");
        personID = input.nextLine();
        System.out.print("Enter Judge Name:");
        personName = input.nextLine();
        System.out.print("Enter mobile No:");
        mobileNo = input.nextLine();
        System.out.print("Enter address:");
        address = input.nextLine();

        Judge jud = new Judge(personID,personName,mobileNo,address);
        jud.insertJudge();

        input.close();
    }

    static void addParticipants()
    {
        Scanner input = new Scanner(System.in);

        String personID;
        String personName;
        String mobileNo;
        String address;

        System.out.print("Enter Participants ID:");
        personID = input.nextLine();
        System.out.print("Enter Participants Name:");
        personName = input.nextLine();
        System.out.print("Enter mobile No:");
        mobileNo = input.nextLine();
        System.out.print("Enter address:");
        address = input.nextLine();

        Participants part = new Participants(personID,personName,mobileNo,address);

        String choice = "Y";

        try 
        {
            JSONParser parser = new JSONParser();  
            JSONObject contests = (JSONObject) parser.parse(new FileReader("contests.json"));

            for(Iterator iterator = contests.keySet().iterator(); iterator.hasNext();) 
            {
                String key = (String) iterator.next();
                JSONObject individualContest = (JSONObject) contests.get(key);
                System.out.println(key+"\t"+individualContest.get("Contest Name"));
            }
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
        
        while(choice.equals("Y") || choice.equals("y"))
        {
            System.out.println("Enter the contests you wld like to participate:");
            choice = input.next();
            part.addParticipating(choice);
            System.out.print("Enter Y to add more or N to exit: ");
            choice = input.next();
        }

        part.insertParticipant();

        input.close();
    }

    static void addContest()
    {
        String contestID;
        String contestName;
        String inChargeId;
        String venueID;
        String date;
        String time;

        Scanner input = new Scanner(System.in);

        System.out.print("Enter contest ID:");
        contestID = input.nextLine();
        System.out.print("Enter contest name:");
        contestName = input.nextLine();

        try 
        {
            JSONParser parser = new JSONParser();  
            JSONObject incharges = (JSONObject) parser.parse(new FileReader("incharge.json"));

            for(Iterator iterator = incharges.keySet().iterator(); iterator.hasNext();) 
            {
                String key = (String) iterator.next();
                JSONObject individualContest = (JSONObject) incharges.get(key);
                System.out.println(key+"\t"+individualContest.get("Incharge Name"));
            }
        } catch (Exception e) 
        {
            e.printStackTrace();
        }

        System.out.print("Select Incharge:");
        inChargeId = input.nextLine();

        try 
        {
            JSONParser parser = new JSONParser();  
            JSONObject venues = (JSONObject) parser.parse(new FileReader("venue.json"));

            for(Iterator iterator = venues.keySet().iterator(); iterator.hasNext();) 
            {
                String key = (String) iterator.next();
                JSONObject individualContest = (JSONObject) venues.get(key);
                System.out.println(key+"\t"+individualContest.get("Venue Name"));
            }
        } catch (Exception e) 
        {
            e.printStackTrace();
        }

        System.out.print("Select Venue:");
        venueID = input.nextLine();
        System.out.print("Enter date:");
        date = input.nextLine();
        System.out.print("Enter Time:");
        time = input.nextLine();

        Contests con = new Contests(contestID, contestName, inChargeId, venueID, date, time);
        con.insertContest();
        input.close();

    }

    static void scoring()
    {
        try {
            JSONParser parser = new JSONParser();  
            JSONObject scores = (JSONObject) parser.parse(new FileReader("scoring.json"));
            JSONObject contests = (JSONObject) parser.parse(new FileReader("contests.json"));

            
            String contest;

            for(Iterator iterator = scores.keySet().iterator(); iterator.hasNext();) 
            {
                String key = (String) iterator.next();
                JSONObject individualContest = (JSONObject) contests.get(key);
                System.out.println(key + "\t" + individualContest.get("Contest Name"));
            }
            System.out.print("Enter a contest ID:");
            Scanner input = new Scanner(System.in);
            contest = input.next();

            String participant;
            System.out.print("Enter Participant ID:");
            participant = input.next();

            int score;
            System.out.print("Enter Score:");
            score =input.nextInt();

            setScore(participant,contest, score);
            input.close();

        } catch (Exception e) 
        {
            e.printStackTrace();
        }    
    }

    static void checkIndiScore()
    {
        String participantID;
        String contestId;

        Scanner input = new Scanner(System.in);

        System.out.print("Enter Particant ID:");
        participantID = input.next();
        System.out.print("Enter Contest ID:");
        contestId = input.next();

        getIndiScore(participantID, contestId);
        input.close();
        
    }
    public static void main(String[] args) 
    {
        int option;

        System.out.println("1:Add Incharge");
        System.out.println("2:Add Judge");
        System.out.println("3:Add Venue");
        System.out.println("4:Add Contest");
        System.out.println("5:Add Participant");
        System.out.println("6:Get Schedule");
        System.out.println("7:Get Participant List");
        System.out.println("8:Scoring");
        System.out.println("9:Publish Result");
        System.out.println("10:View Result");
        System.out.println("11:Check Individual Score");
        System.out.println("13:Exit");

        Scanner sc = new Scanner(System.in);

        //While loop stops after one iteration. Some error with scanner
        
        do
        {
            System.out.print("Enter option:");
            option = sc.next().charAt(0);

            if(option == 1)
            {
                addIncharge();
            }
            else if(option == 2 )
            {
                addJudge();
            }
            else if(option == 3)
            {
                addVenue();
            }
            else if(option == 4)
            {
                addContest();
            }
            else if(option == 5)
            {
                addParticipants();
            }
            else if(option == 6)
            {
                getSchedule();
            }
            else if(option == 7)
            {
                getParticipantList();
            }
            else if(option == 8)
            {
                scoring();
            }
            else if(option == 9)
            {
                publishResult();
            }
            else if(option == 10)
            {
                viewResult();
            }
            else if(option == 12)
            {
                checkIndiScore();
            }
            else if(option == 13)
            {
                System.out.println("Thank You");
                System.exit(0);
            }
            else
            {
                System.out.println("Invalid");
            }
        }while(true); 
    }
}

//Done