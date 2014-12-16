//
//  UIViewController+LelaController.m
//  lela
//
//  Created by Kenneth Lejnieks on 9/26/11.
//  Copyright 2011 Lejnieks Consulting. All rights reserved.
//

#import "UIViewController+lela.h"
#import "lelaViewController.h"

@implementation UIViewController (lela)


-(UIViewController *) applicationController;
{
    lelaViewController * viewController = [[lelaViewController alloc] init];
    return viewController;
}

-(IBAction) nextView:(id)sender
{
    //override in view implementation
}

-(IBAction) previousView:(id)sender
{
    //override in view implementation
}

-(IBAction) gotoView
{
   //override in view implementation 
}

-(void) addView
{
    //override in view implementation
}

-(void) removeView
{
    for (UIView *view in [self.view subviews])
    {
        [view removeFromSuperview];
        //todo: look into profiling this section, 
        //try and remove this better if needed
        //[view release];
        //[view dealloc];
    }
}

-(void) addControls
{
    //
}

@end
