//
//  Step01.m
//  lela
//
//  Created by Kenneth Lejnieks on 9/26/11.
//  Copyright 2011 Lejnieks Consulting. All rights reserved.
//

#import "Step01.h"

//Navigation Tree
#import "Step02.h"
#import "ProductDetails.h"


@implementation Step01

@synthesize scrollView;
@synthesize pageControl;


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
    [super viewDidLoad];
    
    pageControlBeingUsed = NO;
	
    NSArray *images = [NSArray arrayWithObjects:@"quiz_image_1.png", @"quiz_image_2.png", @"quiz_image_3.png", @"quiz_image_4.png", nil];

    for (int i = 0; i < images.count; i++) 
    {
		CGRect frame;
		frame.origin.x = self.scrollView.frame.size.width * i;
		frame.origin.y = 0;
		frame.size = self.scrollView.frame.size;
            
        UIImageView *img = [[UIImageView alloc] initWithFrame:frame];
        img.image = [UIImage imageNamed:[images objectAtIndex:i] ];
		[self.scrollView addSubview:img];
		[img release];
	}
	
	self.scrollView.contentSize = CGSizeMake(self.scrollView.frame.size.width * images.count, self.scrollView.frame.size.height);
	
	self.pageControl.currentPage = 0;
	self.pageControl.numberOfPages = images.count;
}

- (void)scrollViewDidScroll:(UIScrollView *)sender 
{
	if (!pageControlBeingUsed) 
    {
		// Switch the indicator when more than 50% of the previous/next page is visible
		CGFloat pageWidth = self.scrollView.frame.size.width;
		int page = floor((self.scrollView.contentOffset.x - pageWidth / 2) / pageWidth) + 1;
		self.pageControl.currentPage = page;
	}
}

- (void)scrollViewWillBeginDragging:(UIScrollView *)scrollView 
{
	pageControlBeingUsed = NO;
}

- (void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView 
{
	pageControlBeingUsed = NO;
}

- (IBAction)changePage 
{
	// Update the scroll view to the appropriate page
	CGRect frame;
	frame.origin.x = self.scrollView.frame.size.width * self.pageControl.currentPage;
	frame.origin.y = 0;
	frame.size = self.scrollView.frame.size;
	[self.scrollView scrollRectToVisible:frame animated:YES];
	
	// Keep track of when scrolls happen in response to the page control
	// value changing. If we don't do this, a noticeable "flashing" occurs
	// as the the scroll delegate will temporarily switch back the page
	// number.
	pageControlBeingUsed = YES;
}


- (void)viewDidUnload
{
    self.scrollView = nil;
	self.pageControl = nil;
    
    [super viewDidUnload];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

- (void)dealloc 
{
	[scrollView release];
	[pageControl release];
    [super dealloc];
}

///////////////////////////////////////////////////////////////////////////////////////////
-(void) addView
{
    //NB @klejnieks testing this out remove me
    [self.applicationController echo];
    
    //whats my data
    [self.applicationController dump];
    
    //Step02 *nextView = [[Step02 alloc] init];
    ProductDetails *nextView = [[ProductDetails alloc] init];
	[self.view addSubview:nextView.view];
}

-(IBAction) nextView:(id)sender
{
    NSLog(@"Method Call");
    [self removeView];
    [self addView];
}

@end
