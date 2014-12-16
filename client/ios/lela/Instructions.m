//
//  Instructions.m
//  lela
//
//  Created by Kenneth Lejnieks on 9/26/11.
//  Copyright 2011 Lejnieks Consulting. All rights reserved.
//

#import "Instructions.h"
#import "lelaViewController.h"

//Navigation Tree
#import "Step01.h"


@implementation Instructions

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) 
    {
        // 
    }
    return self;
}

- (void)didReceiveMemoryWarning
{
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
}

#pragma mark - View lifecycle

- (void)viewDidLoad
{
    [self.applicationController setTitle:@"Instructions"];
    [super viewDidLoad];
}

- (void)viewDidUnload
{
    [super viewDidUnload];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

- (void)dealloc 
{
    [super dealloc];
}

///////////////////////////////////////////////////////////////////////////////////////////
-(void) addView
{
    [self.applicationController setTitle:@"Step2"];
    Step01 *nextView = [[Step01 alloc] init];
	[self.view addSubview:nextView.view];
}

-(IBAction) nextView:(id)sender
{
    NSLog(@"Method Call");
    
    //lelaViewController * viewController = [[lelaViewController alloc] init];
    
    [self.applicationController showLoggedInTabbar];
    
    // Hide the tab bar
    self.applicationController.hidesBottomBarWhenPushed = YES;
    //viewController.hidesBottomBarWhenPushed = YES;
    [[self navigationController] pushViewController:self.applicationController animated:YES];
    
    [self removeView];
    [self addView];
}

@end
