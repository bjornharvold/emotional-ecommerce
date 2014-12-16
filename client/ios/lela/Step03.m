//
//  Step03.m
//  lela
//
//  Created by Kenneth Lejnieks on 9/28/11.
//  Copyright 2011 Lejnieks Consulting. All rights reserved.
//

#import "Step03.h"

//Navigation Tree
#import "Step04.h"
#import "Scanner.h"
#import "Login.h"


@implementation Step03

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)didReceiveMemoryWarning
{
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
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
    Step04 *nextView = [[Step04 alloc] init];
	[self.view addSubview:nextView.view];
}

-(IBAction) nextView:(id)sender
{
    NSLog(@"Method Call");
    [self removeView];
    [self addView];
}

-(IBAction) gotoLogin:(id)sender
{
    [self removeView];
    
    Login *nextView = [[Login alloc] init];
	[self.view addSubview:nextView.view];
}

-(IBAction) gotoScanner:(id)sender
{
    [self removeView];
    
    Scanner *nextView = [[Scanner alloc] init];
	[self.view addSubview:nextView.view];
}

-(IBAction) searchBarcode:(id)sender
{
    
}

-(IBAction) gotoProductDetail:(id)sender
{
    
}

@end
