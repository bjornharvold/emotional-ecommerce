//
//  LelaTabbar.h
//  lela
//
//  Created by Kenneth Lejnieks on 10/05/11.
//  Copyright 2011 Lejnieks Consulting. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "LelaTabbarDelegate.h"


@protocol LelaTabbarDelegate;


@interface LelaTabbar : UIScrollView <UIScrollViewDelegate, UITabBarDelegate> 
{
	id <LelaTabbarDelegate> lelaTabbarDelegate;
	NSMutableArray *tabBars;
	UITabBar *aTabBar;
	UITabBar *bTabBar;
}

@property (nonatomic, assign) id lelaTabbarDelegate;
@property (nonatomic, retain) NSMutableArray *tabBars;
@property (nonatomic, retain) UITabBar *aTabBar;
@property (nonatomic, retain) UITabBar *bTabBar;

- (id)initWithItems:(NSArray *)items;

// Don't set more items than initially
- (void)setItems:(NSArray *)items animated:(BOOL)animated;
- (int)currentTabBarTag;
- (int)selectedItemTag;

- (BOOL)scrollToTabBarWithTag:(int)tag animated:(BOOL)animated;
- (BOOL)selectItemWithTag:(int)tag;

@end

