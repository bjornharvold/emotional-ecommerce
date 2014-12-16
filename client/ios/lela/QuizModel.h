//
//  QuizModel.h
//  lela
//
//  Created by Kenneth Lejnieks on 10/11/11.
//  Copyright 2011 Lejnieks Consulting. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface QuizModel : NSObject
{
    float *totalNumQuizQuestions;
    NSObject *motivators;
}

@property float *totalNumQuizQuestions;
@property (retain) NSObject *motivators;


@end
