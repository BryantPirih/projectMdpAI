package com.bryant.projectmdpai.UserFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bryant.projectmdpai.Class.ExpertSystem.Facts;
import com.bryant.projectmdpai.Class.ExpertSystem.Rules;
import com.bryant.projectmdpai.Class.ExpertSystem.QuestionES;
import com.bryant.projectmdpai.databinding.FragmentUserExpertBinding;
import com.github.cschen1205.ess.engine.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

public class UserExpertFragment extends Fragment {

    private static final String ARG_PARAM_MENU = "param-menu";

    private FragmentUserExpertBinding binding;
    private String menu;

    private ArrayList<Rules> rules=new ArrayList<>();
    private ArrayList<Facts> facts=new ArrayList<>();
    private ArrayList<QuestionES> questions=new ArrayList<>();

    public UserExpertFragment() {
        // Required empty public constructor
    }

    public static UserExpertFragment newInstance(String paramMenu) {
        UserExpertFragment fragment = new UserExpertFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_MENU, paramMenu);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            menu = getArguments().getString(ARG_PARAM_MENU);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserExpertBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //CALL TO READ DATA FROM DB
        readData( new FirebaseCallback() {
            @Override
            public void onCallbackRules(ArrayList<Rules> list) {
                rules=list;
            }
            @Override
            public void onCallbackFacts(ArrayList<Facts> list) {
                facts=list;
            }
            @Override
            public void onCallbackQuestion(ArrayList<QuestionES> list) {
                questions=list;
            }
        });
    }


    // GET DATAS FROM DB
    private void readData(FirebaseCallback firebaseCallback){
        //rules
        DatabaseReference root = FirebaseDatabase
                .getInstance("https://mdp-project-9db6f-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference();
        DatabaseReference RuleRef = root.child("es/rules");
        RuleRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("Loading rules...");
                for (DataSnapshot ds: dataSnapshot.getChildren() ) {
                    try {
                        String name = ds.child("rulename").getValue().toString();
                        String variable = ds.child("variable").getValue().toString();
                        rules.add(new Rules(name,variable));
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                firebaseCallback.onCallbackRules(rules);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        //Facts
        DatabaseReference FactsRef = root.child("es/facts");
        FactsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("Loading facts...");
                for (DataSnapshot ds: dataSnapshot.getChildren() ) {
                    try {
                        String var = ds.getKey();
                        String value = ds.getValue().toString();
                        facts.add(new Facts(var,value));
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                firebaseCallback.onCallbackFacts(facts);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        //Questions
        DatabaseReference questionsRef = root.child("es/questions");
        questionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("Loading questions...");
                for (DataSnapshot ds: dataSnapshot.getChildren() ) {
                    try {
                        String var = ds.getKey();
                        String value = ds.getValue().toString();
                        questions.add(new QuestionES(var,value));
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                firebaseCallback.onCallbackQuestion(questions);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private interface FirebaseCallback{
        void onCallbackRules(ArrayList<Rules> list);
        void onCallbackQuestion(ArrayList<QuestionES> list);
        void onCallbackFacts(ArrayList<Facts> list);
    }

    ///////////////////////// EXPERT SYSTEM SHELL ///////////////////////////////

    private RuleInferenceEngine getInferenceEngine() //initiate rules here
    {
        RuleInferenceEngine rie=new KieRuleInferenceEngine();

        Rule rule=new Rule("Bicycle");
        rule.addAntecedent(new EqualsClause("vehicleType", "cycle"));
        rule.addAntecedent(new EqualsClause("num_wheels", "2"));
        rule.addAntecedent(new EqualsClause("motor", "no"));
        rule.setConsequent(new EqualsClause("vehicle", "Bicycle"));
        rie.addRule(rule);

        rule=new Rule("Tricycle");
        rule.addAntecedent(new EqualsClause("vehicleType", "cycle"));
        rule.addAntecedent(new EqualsClause("num_wheels", "3"));
        rule.addAntecedent(new EqualsClause("motor", "no"));
        rule.setConsequent(new EqualsClause("vehicle", "Tricycle"));
        rie.addRule(rule);

        rule=new Rule("Motorcycle");
        rule.addAntecedent(new EqualsClause("vehicleType", "cycle"));
        rule.addAntecedent(new EqualsClause("num_wheels", "2"));
        rule.addAntecedent(new EqualsClause("motor", "yes"));
        rule.setConsequent(new EqualsClause("vehicle", "Motorcycle"));
        rie.addRule(rule);

        rule=new Rule("SportsCar");
        rule.addAntecedent(new EqualsClause("vehicleType", "automobile"));
        rule.addAntecedent(new EqualsClause("size", "medium"));
        rule.addAntecedent(new EqualsClause("num_doors", "2"));
        rule.setConsequent(new EqualsClause("vehicle", "Sports_Car"));
        rie.addRule(rule);

        rule=new Rule("Sedan");
        rule.addAntecedent(new EqualsClause("vehicleType", "automobile"));
        rule.addAntecedent(new EqualsClause("size", "medium"));
        rule.addAntecedent(new EqualsClause("num_doors", "4"));
        rule.setConsequent(new EqualsClause("vehicle", "Sedan"));
        rie.addRule(rule);

        rule=new Rule("MiniVan");
        rule.addAntecedent(new EqualsClause("vehicleType", "automobile"));
        rule.addAntecedent(new EqualsClause("size", "medium"));
        rule.addAntecedent(new EqualsClause("num_doors", "3"));
        rule.setConsequent(new EqualsClause("vehicle", "MiniVan"));
        rie.addRule(rule);

        rule=new Rule("SUV");
        rule.addAntecedent(new EqualsClause("vehicleType", "automobile"));
        rule.addAntecedent(new EqualsClause("size", "large"));
        rule.addAntecedent(new EqualsClause("num_doors", "4"));
        rule.setConsequent(new EqualsClause("vehicle", "SUV"));
        rie.addRule(rule);

        rule=new Rule("Cycle");
        rule.addAntecedent(new LessClause("num_wheels", "4"));
        rule.setConsequent(new EqualsClause("vehicleType", "cycle"));
        rie.addRule(rule);

        rule=new Rule("Automobile");
        rule.addAntecedent(new EqualsClause("num_wheels", "4"));
        rule.addAntecedent(new EqualsClause("motor", "yes"));
        rule.setConsequent(new EqualsClause("vehicleType", "automobile"));
        rie.addRule(rule);

        return rie;
    }

    public void testBackwardChain() // simple, conclude from the facts given
    {
        RuleInferenceEngine rie=getInferenceEngine();
        rie.addFact(new EqualsClause("num_wheels", "4"));
        rie.addFact(new EqualsClause("motor", "no"));
        rie.addFact(new EqualsClause("num_doors", "3"));
        rie.addFact(new EqualsClause("size", "medium"));

        System.out.println("Infer: vehicle");

        Vector<Clause> unproved_conditions= new Vector<>();

        Clause conclusion=rie.infer("vehicle", unproved_conditions);

        System.out.println("Conclusion: "+conclusion);
    }

    public void demoBackwardChainWithNullMemory() //ada user input
    {
        RuleInferenceEngine rie=getInferenceEngine();

        System.out.println("Infer with All Facts Cleared:");
        rie.clearFacts();

        Vector<Clause> unproved_conditions= new Vector<>();

        Clause conclusion=null;
        while(conclusion==null)
        {
            conclusion=rie.infer("vehicle", unproved_conditions);
            if(conclusion==null)
            {
                if(unproved_conditions.size()==0)
                {
                    break;
                }
                Clause c=unproved_conditions.get(0);
                System.out.println("ask: "+c+"?");
                unproved_conditions.clear();
                String value=showInputDialog("What is "+c.getVariable()+"?");
                rie.addFact(new EqualsClause(c.getVariable(), value));
            }
        }

        System.out.println("Conclusion: "+conclusion);
        System.out.println("Memory: ");
        System.out.println(rie.getFacts());
    }

    private String showInputDialog(String question) //ask question
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print(question + " ");
        return scanner.next();
    }

    private ArrayList<String> options(String type){ //mendapat pilihan di question tertentu
        //setup options
        ArrayList<String> opt = new ArrayList<>();
        if (type.equalsIgnoreCase("yesno")){
            opt.add("Yes");
            opt.add("No");
        }
        return opt;
    }
}