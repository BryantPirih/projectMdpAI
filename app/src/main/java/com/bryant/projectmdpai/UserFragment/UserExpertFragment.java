package com.bryant.projectmdpai.UserFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
                isDataReady();
            }
            @Override
            public void onCallbackQuestion(ArrayList<QuestionES> list) {
                questions=list;
                isDataReady();
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
    private boolean isDataReady(){
        if (rules.isEmpty()||questions.isEmpty()){
            return false;
        }
        binding.progressBarES.setVisibility(View.INVISIBLE);
        binding.buttonESStart.setVisibility(View.VISIBLE);
        return true;
    }

    private void startES(){
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
            binding.edtESAnswer.setHint("Your Answer (is it "+c.getValue()+"?)");
            binding.tvESQuestion.setText(getQuestion(c.getVariable()));
        }else{
            finished=true;
            toggleVisibility();
        }
        showResult();
    }

    public String getQuestion(String variable){
        //search in list of questions
        for (QuestionES q:questions ) {
            if (q.getForeignvariable().equals(variable)){
                return q.getQuestion();
            }
        }
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
                        String condition ="=";
                        if (ds.hasChild("condition")){
                            condition = ds.child("condition").getValue().toString();
                        }
                        if(ds.hasChild("subGoal")){
                            String subGoal = ds.child("subGoal").getValue().toString();
                            rules.add(new Rules(name,variable,value,condition,subGoal));
                        }else{
                            rules.add(new Rules(name,variable,value,condition));
                        }
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                firebaseCallback.onCallbackRules(rules);
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
    }

    private RuleInferenceEngine getInferenceEngine()
    {
        RuleInferenceEngine rie=new KieRuleInferenceEngine();
        //add empty rule
        for (Rules r:rules) {
            Rule rule=new Rule(r.getRulename());
            boolean exist=false;
            for (Rule ru:rie.getRules()) {
                if (ru.getName().equals(r.getRulename())){
                    exist=true;
                }
            }
            if (!exist){
                rie.addRule(rule);
            }
        }
        //add rule clauses
        for (Rules r:rules){
            for (Rule ru:rie.getRules()) {
                if (ru.getName().equals(r.getRulename())) {
                    updateRule(r, ru);
                }
            }
//            System.out.println(r.getRulename()+":"+r.getVariable()+r.getCondition()+r.getValue());
        }
        System.out.println(rie.getRules());
        return rie;
    }

    private void updateRule(Rules dbrule, Rule rule){
        if (dbrule.getCondition().equals(">")){
            rule.addAntecedent(new GreaterClause(dbrule.getVariable(), dbrule.getValue()));
        }if (dbrule.getCondition().equals("<")){
            rule.addAntecedent(new LessClause(dbrule.getVariable(), dbrule.getValue()));
        }if (dbrule.getCondition().equals("=")){
            rule.addAntecedent(new EqualsClause(dbrule.getVariable(), dbrule.getValue()));
        }
        if(dbrule.getSubGoal().equals("")){
            rule.setConsequent(new EqualsClause(goal, dbrule.getRulename()));
        }else{
            rule.setConsequent(new EqualsClause(dbrule.getSubGoal(), dbrule.getRulename()));
        }
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