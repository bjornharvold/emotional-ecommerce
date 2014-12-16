//
//  SampleButton.h
//  lela
//
//  Created by Kenneth Lejnieks on 9/23/11.
//  Copyright 2011 Lejnieks Consulting. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SampleButton : UIButton
{
    @private NSString *_temp;
}

@property (nonatomic, retain) NSString *temp;

@end
