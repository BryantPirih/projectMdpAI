package com.bryant.projectmdpai.UserFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import java.util.Stack;
import java.util.Vector;

public class UserExpertFragment extends Fragment {

    private static final String ARG_PARAM_MENU = "param-menu";

    private FragmentUserExpertBinding binding;
    private String menu;

    private ArrayList<Rules> rules=new ArrayList<>();
    private ArrayList<Facts> facts=new ArrayList<>();
    private ArrayList<QuestionES> questions=new ArrayList<>();

    private RuleInferenceEngine rie;
    private Vector<Clause> unproved_conditions;
    private Clause conclusion;
    private Clause c;
    private boolean finished;
    private String goal = "vehicle";
    private Stack<RuleInferenceEngine> pastRie;


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
        binding.buttonESNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFact();
            }
        });
        binding.buttonESStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startES();
            }
        });
        binding.buttonESBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                undo();
            }
        });
    }

    private void startES(){
        if (rules.isEmpty()||facts.isEmpty()||questions.isEmpty()){ //only starts when all data is not empty
            return;
        }
        rie=getInferenceEngine();
        unproved_conditions= new Vector<>();
        pastRie =new Stack<>();
        conclusion=null;
        c = null;
        finished=false;
        rie.clearFacts();
        toggleVisibility();
        nextQuestion();
    }

    public void nextQuestion(){
        conclusion=rie.infer( goal , unproved_conditions);
        if(unproved_conditions.size()>0)
        {
            c=unproved_conditions.get(0);
            System.out.println("ask: "+c+"?");
            unproved_conditions.clear();
            binding.edtESAnswer.setHint("Your Answer (prediction : "+c+"?)");
            binding.tvESQuestion.setText(getQuestion(c.getVariable()));
        }else{
            finished=true;
            toggleVisibility();
        }
        showResult();
    }
    public String getQuestion(String variable){
        //search in list of questions
        return variable+" ?";
    }

    public void addFact(){
        String input = binding.edtESAnswer.getText().toString();
        if (input.equals(""))
        {
            Toast.makeText(getActivity(), "Input kosong", Toast.LENGTH_SHORT).show();
            return;
        }
        pastRie.push(rie);
        rie.addFact(new EqualsClause(c.getVariable(), input));
        binding.edtESAnswer.setText("");
        toggleVisibility();
        nextQuestion();
    }

    public void undo(){
        if (!pastRie.isEmpty()){
            rie = pastRie.pop();
            toggleVisibility();
            nextQuestion();
        }
    }

    public void showResult(){
        String con="";
        String memory = "Memory : "+rie.getFacts();
        if (finished){
            if (conclusion==null){
                con = "Conclusion : Sorry, we cannot find solution from our database\n\n";
            }else{
                con = "Conclusion : "+conclusion+"\n\n";
            }
        }
        binding.tvESConclusion.setText(con+memory);
    }

    public void toggleVisibility(){
        if(finished){
            binding.buttonESBack.setVisibility(View.INVISIBLE);
            binding.buttonESNext.setVisibility(View.INVISIBLE);
            binding.edtESAnswer.setVisibility(View.INVISIBLE);
            binding.tvESQuestion.setVisibility(View.INVISIBLE);
            binding.textView39.setVisibility(View.INVISIBLE);
            binding.buttonESStart.setVisibility(View.VISIBLE);
        }else{
            binding.buttonESStart.setVisibility(View.INVISIBLE);
            binding.buttonESNext.setVisibility(View.VISIBLE);
            if  (!pastRie.isEmpty()){
                //binding.buttonESBack.setVisibility(View.VISIBLE);
            }else{
                binding.buttonESBack.setVisibility(View.INVISIBLE);
            }
            binding.edtESAnswer.setVisibility(View.VISIBLE);
            binding.tvESQuestion.setVisibility(View.VISIBLE);
            binding.textView39.setVisibility(View.VISIBLE);
            binding.textView41.setVisibility(View.VISIBLE);
            binding.tvESConclusion.setVisibility(View.VISIBLE);
        }
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
                        String value = ds.child("value").getValue().toString();
                        rules.add(new Rules(name,variable,value));
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

    private RuleInferenceEngine getInferenceEngine() //initiate rules here
    {
        RuleInferenceEngine rie=new KieRuleInferenceEngine();

        for (Rules r:rules) {
            Rule rule=new Rule(r.getRulename());

            rule.addAntecedent(new EqualsClause("vehicleType", "cycle"));
            rule.addAntecedent(new EqualsClause("num_wheels", "2"));
            rule.addAntecedent(new EqualsClause("motor", "no"));
            rule.setConsequent(new EqualsClause("vehicle", "Bicycle"));
            rie.addRule(rule);
        }

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

    ///
    private ArrayList<String> options(String type){ //mendapat pilihan di question tertentu
        ArrayList<String> opt = new ArrayList<>();
        if (type.equalsIgnoreCase("yesno")){
            opt.add("Yes");
            opt.add("No");
        }
        return opt;
    }
}