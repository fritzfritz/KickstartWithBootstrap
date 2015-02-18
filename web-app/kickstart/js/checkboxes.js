if (typeof jQuery !== 'undefined') {
	$(document).ready(function() {
		var $body = $('body');
		/**
		 * A checkbox using Bootstrap CSS's radio buttons
		 */
		$body.on('click.checkbox','.radiocheckbox .btn.on:not(.readonly)',function() {
			var $checkbox = $(this).parent().siblings("input[type=checkbox]");
			$checkbox.attr('checked', true);
            $checkbox.change();
			$(this).addClass("btn-primary");
			$(this).siblings().removeClass("btn-primary");
			var group = $checkbox.data('group');
			if (group) {
				$.each(group.split(','), function (idx, checkbox) {
					if ($(checkbox).attr('checked')) {
						$(checkbox).attr('checked', false);
						$(checkbox).siblings('.radiocheckbox').children('.btn.on').each(function (idx, other) {
							$(other).removeClass("btn-primary");
							$(other).removeClass("active");
							$(other).siblings().addClass("btn-primary");
							$(other).siblings().addClass("active");
						})
					}
				});
			}
		})
        $body.on('click.checkbox','.radiocheckbox .btn.off:not(.readonly)',function() {
			var $checkbox = $(this).parent().siblings("input[type=checkbox]");
			$checkbox.attr('checked', false);
            $checkbox.change();
			$(this).addClass("btn-primary");
			$(this).siblings().removeClass("btn-primary");
		})
		
		/**
		 * A checkbox using Bootstrap CSS's buttons
		 */
        $body.on('click.checkbox','.buttoncheckbox:not(.readonly)',function() {
			var $text = $(this).text();
			$(this).text($(this).attr('data-complete-text'));
			$(this).attr('data-complete-text', $text);
			$(this).toggleClass("btn-primary");
			$(this).removeClass("active");
			var $checkbox = $(this).siblings("input[type=checkbox]");
			$checkbox.attr('checked', !$checkbox.attr('checked'));
            $checkbox.change();
		})
	});
}
