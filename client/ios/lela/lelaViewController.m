//
//  lelaViewController.m
//  lela
//
//  Created by Kenneth Lejnieks on 9/22/11.
//  Copyright 2011 Lejnieks Consulting. All rights reserved.
//

#import "lelaViewController.h"

//Navigation Tree
#import "Instructions.h"
#import "LoginWithFacebook.h"
#import "Login.h"
#import "About.h"

@implementation lelaViewController


@synthesize applicationModel;
@synthesize header;
@synthesize tabBar;
@synthesize dLabel;
@synthesize fLabel;


-(void) echo
{
    NSLog(@"ECHO");
}

-(void) dump
{
    NSLog(@"Dumping Data");
    NSLog(@"%@", [applicationModel.sessionModel getMockUDID]);
}

-(void) setTitle:(NSString *)value;
{
    NSLog(@"Setting Header Title:%@", value);
	header.topItem.title = @"TESTING";
    self.navigationItem.title = @"HEllo World";
}

- (void)didReceiveMemoryWarning
{
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    header = [[UINavigationBar alloc] init];
    applicationModel = [[ApplicationModel alloc] init];

    
	UITabBarItem *loginWithFacebookTab = [[UITabBarItem alloc] initWithTitle:@"Login with Facebook" image:[UIImage imageNamed:@"fpoicon1.png"] tag:300];
	UITabBarItem *loginTab = [[UITabBarItem alloc] initWithTitle:@"Login" image:[UIImage imageNamed:@"fpoicon2.png"] tag:301];
	UITabBarItem *aboutTab = [[UITabBarItem alloc] initWithTitle:@"About" image:[UIImage imageNamed:@"fpoicon3.png"] tag:302];
	
	self.tabBar = [[LelaTabbar alloc] initWithItems:[NSArray arrayWithObjects:loginWithFacebookTab, loginTab, aboutTab, nil]];
	
	[loginWithFacebookTab release];
	[loginTab release];
	[aboutTab release];
	
	self.tabBar.lelaTabbarDelegate = self;
	
	[self.view addSubview:self.tabBar];
    
    
	/*
     // TEST TABBAR
     UIButton *aButton = [UIButton buttonWithType:UIButtonTypeRoundedRect];
     [aButton addTarget:self action:@selector(showLoggedInTabbar) forControlEvents:UIControlEventTouchUpInside];
     aButton.frame = CGRectMake(20.0, 90.0, 280.0, 37.0);
     [aButton setTitle:@"Show Logged-In tabbar" forState:UIControlStateNormal];
     
     [self.view addSubview:aButton];
     
     UIButton *bButton = [UIButton buttonWithType:UIButtonTypeRoundedRect];
     [bButton addTarget:self action:@selector(showLoggedOutTabbar) forControlEvents:UIControlEventTouchUpInside];
     bButton.frame = CGRectMake(20.0, 135.0, 280.0, 37.0);
     [bButton setTitle:@"Show Logged-Out tabbar" forState:UIControlStateNormal];
     
     [self.view addSubview:bButton];
     
     UIButton *eButton = [UIButton buttonWithType:UIButtonTypeRoundedRect];
     [eButton addTarget:self action:@selector(selectItem8) forControlEvents:UIControlEventTouchUpInside];
     eButton.frame = CGRectMake(20.0, 270.0, 280.0, 37.0);
     [eButton setTitle:@"Select item 8" forState:UIControlStateNormal];
     
     [self.view addSubview:eButton];
     
     UILabel *cLabel = [[UILabel alloc] initWithFrame:CGRectMake(20.0, 315.0, 230.0, 21.0)];
     cLabel.text = @"Current tab bar:";
     
     [self.view addSubview:cLabel];
     
     [cLabel release];
     
     self.dLabel = [[UILabel alloc] initWithFrame:CGRectMake(258.0, 315.0, 42.0, 21.0)];
     self.dLabel.text = @"1";
     self.dLabel.textAlignment = UITextAlignmentRight;
     
     [self.view addSubview:self.dLabel];
     
     UILabel *eLabel = [[UILabel alloc] initWithFrame:CGRectMake(20.0, 344.0, 230.0, 21.0)];
     eLabel.text = @"Selected item:";
     
     [self.view addSubview:eLabel];
     
     [eLabel release];
     
     self.fLabel = [[UILabel alloc] initWithFrame:CGRectMake(258.0, 344.0, 42.0, 21.0)];
     self.fLabel.textAlignment = UITextAlignmentRight;
     
     [self.view addSubview:self.fLabel];
     */
	
}

-(void) viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    [self addView];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

-(void) addView
{
    UIView *container = [self.view.window viewWithTag:100];
    
    Instructions *currentView = [[Instructions alloc] init];
	[container addSubview:currentView.view];
    //[currentView release]; 
}

-(IBAction) back:(id)sender
{
    [self removeView];
    [self addView];
}

-(void) showLoggedInTabbar
{
	UITabBarItem *myListTab = [[UITabBarItem alloc] initWithTitle:@"My Lela List" image:[UIImage imageNamed:@"fpoicon1.png"] tag:303]; 
    UITabBarItem *scanTab = [[UITabBarItem alloc] initWithTitle:@"Scan" image:[UIImage imageNamed:@"fpoicon2.png"] tag:304];
	UITabBarItem *browseTab = [[UITabBarItem alloc] initWithTitle:@"Browse" image:[UIImage imageNamed:@"fpoicon3.png"] tag:305];
	UITabBarItem *more = [[UITabBarItem alloc] initWithTabBarSystemItem:UITabBarSystemItemMore tag:306];
	
    myListTab.badgeValue = @"1";
	
    [self.tabBar setItems:[NSArray arrayWithObjects:myListTab, scanTab, browseTab, more, nil] animated:YES];
	
	[myListTab release];
	[scanTab release];
	[browseTab release];
	[more release];
}

-(void) showLoggedOutTabbar 
{
    NSLog(@"Showing Logged in tabbar");
    
    UITabBarItem *loginWithFacebookTab = [[UITabBarItem alloc] initWithTitle:@"Login with Facebook" image:[UIImage imageNamed:@"fpoicon1.png"] tag:300];
	UITabBarItem *loginTab = [[UITabBarItem alloc] initWithTitle:@"Login" image:[UIImage imageNamed:@"fpoicon2.png"] tag:301];
	UITabBarItem *aboutTab = [[UITabBarItem alloc] initWithTitle:@"About" image:[UIImage imageNamed:@"fpoicon3.png"] tag:302];
    
	[self.tabBar setItems:[NSArray arrayWithObjects:loginWithFacebookTab, loginTab, aboutTab, nil] animated:YES];
	
	[loginWithFacebookTab release];
	[loginTab release];
	[aboutTab release];
    
}

-(void) selectItem8 
{
	[self.tabBar selectItemWithTag:7];
}

-(void) removeView
{
    UIView *container = [self.view.window viewWithTag:100];   
    for (UIView *view in [container subviews])
    {
        [view removeFromSuperview];
    }
}

-(void) lelaTabbar:(LelaTabbar *)tabBar didScrollToTabBarWithTag:(int)tag 
{
	self.dLabel.text = [NSString stringWithFormat:@"%d", tag + 1];
}

-(void) lelaTabbar:(LelaTabbar *)tabBar didSelectItemWithTag:(int)tag 
{
    UIView *container = [self.view.window viewWithTag:100];   
    
    switch (tag) 
    {
        case 300:
            [self removeView]; 
            LoginWithFacebook *loginWithFacebook = [[LoginWithFacebook alloc] init];
            [container addSubview:loginWithFacebook.view];
            break;
            
        case 301:
            [self removeView]; 
            Login *login = [[Login alloc] init];
            [container addSubview:login.view];
            break;
            
        case 302:
            [self removeView]; 
            About *about = [[About alloc] init];
            [container addSubview:about.view];
            break;
            
        case 303:
            [self removeView]; 
            Login *myList = [[Login alloc] init];
            [container addSubview:myList.view];
            break;
            
        case 304:
            [self removeView]; 
            Login *scan = [[Login alloc] init];
            [container addSubview:scan.view];
            break;
            
        case 305:
            [self removeView]; 
            Login *browse = [[Login alloc] init];
            [container addSubview:browse.view];
            break;
            
        case 306:
            [self removeView]; 
            Login *more = [[Login alloc] init];
            [container addSubview:more.view];
            break;
            
        default:
            //
            break;
    }
    
	self.fLabel.text = [NSString stringWithFormat:@"%d", tag + 1];
}

- (void)dealloc 
{
    [fLabel release];
	[dLabel release];
	
	[tabBar release];
    
    [applicationModel release];
    
    [super dealloc];
}

@end
