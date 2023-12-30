package com.example.jettriviaapplication.component

import android.provider.MediaStore.Audio.Radio
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle

import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jettriviaapplication.model.QuestionItem
import com.example.jettriviaapplication.screens.QuestionViewModel
import com.example.jettriviaapplication.util.AppColors

@Composable
fun Questions(viewmodel: QuestionViewModel) {
    val questions=viewmodel.data.value.data?.toMutableList()

    val questionInd = remember {
        mutableStateOf(0)
    }
    if(viewmodel.data.value.loading==true)
    {
        CircularProgressIndicator()

    }
    else
    {
        val question = try {
            questions?.get(questionInd.value)

        }catch (e:Exception){null}
        if(questions !=null ){
            QuestionDisplay(question = question!!,questionInd=questionInd,
            viewmodel=viewmodel){
                questionInd.value=questionInd.value+1
            }
        }
    }
}

//@Preview
@Composable
fun QuestionDisplay(
    question: QuestionItem,
    questionInd:MutableState<Int>,
    viewmodel: QuestionViewModel,
    onNetClicked:(Int) -> Unit={}
){
    val choicesState= remember(question){
        question.choices.toMutableList()
    }
    val answerState=remember(question){
        mutableStateOf<Int?>(null)
    }
    val correctAnswerState = remember(question) {
        mutableStateOf<Boolean?>(null)
    }
    val updateAnswer :(Int) ->Unit = remember(question) {
        {
            answerState.value=it
            correctAnswerState.value=choicesState[it] ==question.answer
        }
    }
    val patheffect= PathEffect.dashPathEffect(floatArrayOf(10f,10f),0f)
    Surface(modifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth(),
    color=AppColors.mDarkPurple) {
        Column(modifier = Modifier.padding(12.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start) {

            if(questionInd.value >=3) ShowProgressBar(score = questionInd.value)
            QuestionTracker(counter= questionInd.value+1, OutOff = viewmodel.getTotalQuestionCount())
            DrawDottedLine(pathEffect = patheffect)
            Column {
                Text(text = question.question,
                    modifier = Modifier
                        .padding(6.dp)
                        .fillMaxHeight(0.3f)
                        .align(Alignment.Start)
                    ,fontSize = 17.sp,
                    color=AppColors.mOffWhite,
                    fontWeight = FontWeight.Bold, lineHeight = 22.sp
                )

                choicesState.forEachIndexed { index, answerText->
                    Row(modifier = Modifier
                        .padding(3.dp)
                        .fillMaxWidth()
                        .height(45.dp)
                        .border(
                            width = 4.dp, brush = Brush.linearGradient(
                                colors = listOf(AppColors.mDarkPurple, AppColors.mOffDarkPurple)
                            ), shape = RoundedCornerShape(15.dp)
                        )
                        .clip(
                            RoundedCornerShape(
                                topStartPercent = 50, topEndPercent = 50, bottomEndPercent = 50,
                                bottomStartPercent = 50
                            )
                        )
                        .background(Color.Transparent),
                     verticalAlignment = Alignment.CenterVertically){
                        RadioButton(selected = (answerState.value==index),
                            onClick = {
                                updateAnswer(index)
                            },
                          modifier = Modifier.padding(start = 16.dp),
                            colors = RadioButtonDefaults.colors(
                                selectedColor = if(correctAnswerState.value==true
                                    && index ==answerState.value){
                                    Color.Green.copy(alpha = 0.2f)
                                }
                            else
                                {
                                    Color.Red.copy(alpha = 0.2f)
                                })) // end of radio button
                        val annotatedString = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Light,
                            color= if(correctAnswerState.value==true
                                && index ==answerState.value){
                                Color.Green
                            }
                            else if(correctAnswerState.value ==false && index==answerState.value)
                            {
                                Color.Red
                            }
                            else
                            {
                                AppColors.mOffWhite
                            }, fontSize = 17.sp)){
                                append(answerText)
                            }
                        }
                        Text(text = annotatedString, modifier = Modifier.padding(6.dp))
                    }
                }
                Spacer(modifier =Modifier.height(12.dp))
                Button(onClick = { onNetClicked(questionInd.value) },
                modifier = Modifier
                    .padding(3.dp)
                    .align(Alignment.CenterHorizontally),
                shape=RoundedCornerShape(34.dp), colors = ButtonDefaults.buttonColors(
                        backgroundColor = AppColors.mLightBlue
                )) {
                    Text(text = "Next", modifier = Modifier.padding(4.dp),
                    color=AppColors.mOffWhite, fontSize = 17.sp)

                }
            }

        }

    }
}

@Composable
fun DrawDottedLine(pathEffect: PathEffect){
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp),
        ){
         drawLine(AppColors.mLightGray, start = Offset(0f,0f),
         end = Offset(size.width,0f)
         )
    }
}


@Preview
@Composable
fun ShowProgressBar(score :Int=12){
    val gradient= Brush.linearGradient(listOf(Color(0xFFF95075),
    Color(0xFFBE6BE5 )))
    val progressFactor = remember(score ) {
        mutableStateOf(score*0.005f)
    }
    Row(modifier = Modifier
        .padding(3.dp)
        .fillMaxWidth()
        .height(45.dp)
        .border(
            width = 4.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    AppColors.mLightPurple, AppColors.mLightPurple
                )
            ), shape = RoundedCornerShape(34.dp)
        )
        .clip(
            RoundedCornerShape(
                topStartPercent = 50, topEndPercent = 50,
                bottomStartPercent = 50, bottomEndPercent = 50
            )
        )
        .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically) {
        Button(
            contentPadding = PaddingValues(1.dp),
            onClick = {}, modifier = Modifier
                .fillMaxWidth(progressFactor.value)
                .background(brush = gradient),
        enabled = false, elevation = null, colors = buttonColors(
                backgroundColor = Color.Transparent,
                disabledBackgroundColor = Color.Transparent
        )) {
                Text(
                    text = (score * 10).toString(),
                    modifier = Modifier.clip(shape = RoundedCornerShape(23.dp))
                        .fillMaxHeight(0.87f).fillMaxWidth().padding(6.dp),
                    color=AppColors.mOffWhite, textAlign = TextAlign.Center
                )
        }
        
    }

}


@Composable
fun QuestionTracker(counter:Int =10, OutOff:Int =100){
    Text(text = buildAnnotatedString {
       withStyle(style = ParagraphStyle(textIndent = TextIndent.None)){
           withStyle(style = SpanStyle(color =AppColors.mLightBlue,
           fontWeight = FontWeight.Bold,
           fontSize = 27.sp)){
               append("Question $counter/")
               withStyle(style= SpanStyle(AppColors.mLightGray, fontWeight = FontWeight.Light,
               fontSize = 14.sp)){
                   append("$OutOff")
               }
           }
       }
    },
         modifier = Modifier.padding(20.dp))

}


