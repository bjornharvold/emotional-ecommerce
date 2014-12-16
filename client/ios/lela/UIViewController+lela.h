//
//  UIViewController+LelaController.h
//  lela
//
//  Created by Kenneth Lejnieks on 9/26/11.
//  Copyright 2011 Lejnieks Consulting. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "lelaViewController.h"


//@class SampleButton;


@interface UIViewController (lela)

#define applicationDelegate [[UIApplication sharedApplication] delegate];

-(UIViewController *) applicationController;

-(IBAction) nextView:(id)sender;
-(IBAction) previousView:(id)sender;
-(IBAction) gotoView;

-(void) addView;
-(void) removeView;
-(void) addControls;

@end
