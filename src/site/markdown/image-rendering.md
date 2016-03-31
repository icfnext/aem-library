## Image Rendering

### Overview

AEM has default servlets for handling page and component level images.  However, the default image servlets only allow for a single image per page or component; the custom AEM Library Image Servlet adds the ability to provide additional selectors to specify named images and a width value for resizing the rendered image.

### Pages

The Page Properties dialog can be customized to add additional named images as follows:

	<secondimage jcr:primaryType="cq:Widget"
	    cropParameter="./secondimage/imageCrop"
	    ddGroups="[media]"
	    fileNameParameter="./secondimage/fileName"
	    fileReferenceParameter="./secondimage/fileReference"
	    mapParameter="./secondimage/imageMap"
	    name="./secondimage/file"
	    renditionSuffix="/_jcr_content/renditions/original"
	    requestSuffix=".img.secondimage.png"
	    rotateParameter="./secondimage/imageRotate"
	    title="Second Image"
	    xtype="html5smartimage" />

The following URLs can be used to access the default and named images for a given page.  The corresponding AEM Library page decorator method names are listed as well.

URL                                      | Image Name        | Width | Method Name
:----------------------------------------|:------------------|:------|:----------------------------------
/content/home.img.png                 | "image" (Default) | Full  | `getImageSource()`
/content/home.img.100.png             | "image" (Default) | 100   | `getImageSource(100)`
/content/home.img.secondimage.png     | "secondimage"     | Full  | `getImageSource("secondimage")`
/content/home.img.secondimage.100.png | "secondimage"     | 100   | `getImageSource("secondimage", 100)`

As illustrated in the table above, "image" is the default name for page images (in accordance with the foundation page dialog definition).

### Components

Component-level images are accessed in the same manner as page images; the examples below illustrate how the `dialog.xml` aligns with the image URL.

Single image:

	<image jcr:primaryType="cq:Widget"
	    cropParameter="./image/imageCrop"
	    ddGroups="[media]"
	    fileNameParameter="./image/fileName"
	    fileReferenceParameter="./image/fileReference"
	    mapParameter="./image/imageMap"
	    name="./image/file"
	    renditionSuffix="/_jcr_content/renditions/original"
	    requestSuffix=".img.image.png"
	    rotateParameter="./image/imageRotate"
	    title="Image"
	    xtype="html5smartimage" />

Named image (for adding multiple images to a component):

	<secondimage jcr:primaryType="cq:Widget"
	    cropParameter="./secondimage/imageCrop"
	    ddGroups="[media]"
	    fileNameParameter="./secondimage/fileName"
	    fileReferenceParameter="./secondimage/fileReference"
	    mapParameter="./secondimage/imageMap"
	    name="./secondimage/file"
	    renditionSuffix="/_jcr_content/renditions/original"
	    requestSuffix=".img.secondimage.png"
	    rotateParameter="./secondimage/imageRotate"
	    title="Second Image"
	    xtype="html5smartimage" />

The following URLs can be used to access the default and named images for a given component.  The corresponding AEM Library component (i.e. node) decorator method names are listed as well.

URL                                                                | Image Name        | Width | Method Name
:------------------------------------------------------------------|:------------------|:------|:-----------
/content/home/jcr:content/par/component.img.png                 | "image" (Default) | Full  | `getImageSource()`
/content/home/jcr:content/par/component.img.100.png             | "image" (Default) | 100   | `getImageSource(100)`
/content/home/jcr:content/par/component.img.secondimage.png     | "secondimage"     | Full  | `getImageSource("secondimage")`
/content/home/jcr:content/par/component.img.secondimage.100.png | "secondimage"     | 100   | `getImageSource("secondimage", 100)`

As with pages, we specify "image" as the default image name to align with the page-level convention.

### Tag Library

As detailed on the [Tag Library](https://github.com/Citytechinc/aem-library/wiki/tag-library) wiki page, the Image Source tag can be used to render URLs directly in JSPs without calling the above methods in a component Java class.

    <img src="<aem-library:imageSource name="secondimage" width="100"/>">