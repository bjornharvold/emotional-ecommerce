//
//  LoginWithFacebook.m
//  lela
//
//  Created by Kenneth Lejnieks on 10/7/11.
//  Copyright 2011 Lejnieks Consulting. All rights reserved.
//

#import "LoginWithFacebook.h"
#import "lelaAppDelegate.h"

@implementation LoginWithFacebook

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

-(IBAction) loginWithFacebook:(id)sender
{
    lelaAppDelegate *appDelegate = (lelaAppDelegate *)[[UIApplication sharedApplication] delegate];
    [appDelegate.viewController showLoggedInTabbar];

}

@end
