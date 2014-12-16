//
//  ProductModel.m
//  lela
//
//  Created by Kenneth Lejnieks on 10/11/11.
//  Copyright 2011 Lejnieks Consulting. All rights reserved.
//

#import "ProductModel.h"

@implementation ProductModel

@synthesize lpid;
@synthesize title;
@synthesize subtitle;
@synthesize image;
@synthesize images;
@synthesize prices;
@synthesize local;
@synthesize online;
@synthesize colors;
@synthesize shortDescription;
@synthesize description;
@synthesize features;
@synthesize specifications;
@synthesize hasAccessories;
@synthesize vendors;
@synthesize hasWebsite;
@synthesize onlinePurchase;
@synthesize ratings;


- (id)init
{
    if ((self = [super init]))
    {
        // Initialization code here.
    }
    
    return self;
}

@end


