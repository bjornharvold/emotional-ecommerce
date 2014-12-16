//
//  Step01.h
//  lela
//
//  Created by Kenneth Lejnieks on 9/26/11.
//  Copyright 2011 Lejnieks Consulting. All rights reserved.
//

#import "UIViewController+lela.h"


@interface Step01 : UIViewController <UIScrollViewDelegate>
{
    UIScrollView *scrollView;
	UIPageControl *pageControl;
	
	BOOL pageControlBeingUsed;
}


@property (nonatomic, retain) IBOutlet UIScrollView *scrollView;
@property (nonatomic, retain) IBOutlet UIPageControl *pageControl;

- (IBAction)changePage;

@end
