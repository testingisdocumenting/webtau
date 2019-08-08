def split = $('#split ul li')
split.should == [100, lessThan(100), greaterThanOrEqual(150)]