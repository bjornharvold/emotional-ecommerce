//
//  Scanner.h
//  lela
//
//  Created by Kenneth Lejnieks on 9/29/11.
//  Copyright 2011 Lejnieks Consulting. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <QuartzCore/QuartzCore.h>

@interface Scanner : UIViewController <ZBarReaderDelegate>
{
    UIImageView *resultImage;
    UITextView *resultText;
}

@property (nonatomic, retain) IBOutlet UIImageView *resultImage;
@property (nonatomic, retain) IBOutlet UITextView *resultText;

-(IBAction) scanButtonTapped;

@end
